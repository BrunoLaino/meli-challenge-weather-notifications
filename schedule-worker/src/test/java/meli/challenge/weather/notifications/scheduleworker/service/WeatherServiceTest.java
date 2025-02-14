package meli.challenge.weather.notifications.scheduleworker.service;

import meli.challenge.weather.notifications.scheduleworker.feign.CptecFeignClient;
import meli.challenge.weather.notifications.scheduleworker.model.dto.CityWeatherForecast;
import meli.challenge.weather.notifications.scheduleworker.model.dto.DailyForecast;
import meli.challenge.weather.notifications.scheduleworker.model.dto.WaveForecast;
import meli.challenge.weather.notifications.scheduleworker.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private CptecFeignClient cptecFeignClient;

    @InjectMocks
    private WeatherService weatherService;

    private final String forecastXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
            + "<cidade><nome>São Paulo</nome><uf>SP</uf>"
            + "<previsao><dia>2025-02-15</dia><tempo>Ensolarado</tempo><minima>18</minima><maxima>26</maxima></previsao>"
            + "<previsao><dia>2025-02-16</dia><tempo>Parcialmente Nublado</tempo><minima>17</minima><maxima>25</maxima></previsao>"
            + "</cidade>";

    private final String cityListXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
            + "<cidades><cidade><nome>São Paulo</nome><uf>SP</uf><id>12345</id></cidade></cidades>";

    private final String waveXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
            + "<onda><nome>Rio de Janeiro</nome><uf>RJ</uf>"
            + "<manha><agitacao>Moderada</agitacao><altura>1.5</altura><direcao>Norte</direcao><vento>10</vento><vento_dir>NNE</vento_dir></manha>"
            + "<tarde><agitacao>Alta</agitacao><altura>2.0</altura><direcao>Nordeste</direcao><vento>12</vento><vento_dir>ENE</vento_dir></tarde>"
            + "<noite><agitacao>Baixa</agitacao><altura>1.0</altura><direcao>Sul</direcao><vento>8</vento><vento_dir>SSW</vento_dir></noite>"
            + "</onda>";

    private final String undefinedWaveXml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
            + "<onda><nome>undefined</nome><uf></uf>"
            + "<manha><agitacao></agitacao><altura></altura><direcao></direcao><vento></vento><vento_dir></vento_dir></manha>"
            + "<tarde><agitacao></agitacao><altura></altura><direcao></direcao><vento></vento><vento_dir></vento_dir></tarde>"
            + "<noite><agitacao></agitacao><altura></altura><direcao></direcao><vento></vento><vento_dir></vento_dir></noite>"
            + "</onda>";

    @Test
    void testGetForecastWithCityId() {
        when(cptecFeignClient.getFourDayCityForecast("12345")).thenReturn(forecastXml);
        CityWeatherForecast forecast = weatherService.getForecast("São Paulo", "12345");
        assertEquals("São Paulo", forecast.getCityName());
        assertEquals("SP", forecast.getUf());
        assertEquals(2, forecast.getDailyForecasts().size());
        DailyForecast firstDay = forecast.getDailyForecasts().get(0);
        assertEquals(LocalDate.parse("2025-02-15"), firstDay.getDate());
        assertEquals("Ensolarado", firstDay.getWeather());
        assertEquals(18, firstDay.getMinTemp());
        assertEquals(26, firstDay.getMaxTemp());
    }

    @Test
    void testGetForecastWithoutCityId() {
        when(cptecFeignClient.findCitiesByName("São Paulo")).thenReturn(cityListXml);
        when(cptecFeignClient.getFourDayCityForecast("12345")).thenReturn(forecastXml);
        CityWeatherForecast forecast = weatherService.getForecast("São Paulo", "");
        assertEquals("São Paulo", forecast.getCityName());
        assertEquals("SP", forecast.getUf());
        assertEquals(2, forecast.getDailyForecasts().size());
    }

    @Test
    void testDiscoverCityIdByNameFound() {
        when(cptecFeignClient.findCitiesByName("São Paulo")).thenReturn(cityListXml);
        String id = weatherService.discoverCityIdByName("São Paulo");
        assertEquals("12345", id);
    }

    @Test
    void testDiscoverCityIdByNameNotFound() {
        when(cptecFeignClient.findCitiesByName("Unknown")).thenReturn("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><cidades></cidades>");
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                weatherService.discoverCityIdByName("Unknown"));
        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void testGetWaveForecastValid() {
        when(cptecFeignClient.getCityWaveForecastForDay("12345", 0)).thenReturn(waveXml);
        WaveForecast wf = weatherService.getWaveForecast("12345");
        assertNotNull(wf);
        assertEquals("Rio de Janeiro", wf.getCityName());
        assertEquals("RJ", wf.getUf());
        assertNotNull(wf.getManha());
        assertEquals("Moderada", wf.getManha().getAgitacao());
        assertEquals("1.5", wf.getManha().getAltura());
    }

    @Test
    void testGetWaveForecastUndefined() {
        when(cptecFeignClient.getCityWaveForecastForDay("12345", 0)).thenReturn(undefinedWaveXml);
        WaveForecast wf = weatherService.getWaveForecast("12345");
        assertNull(wf);
    }

    @Test
    void testRecoverGetForecast() {
        Exception ex = new Exception("error");
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                weatherService.recoverGetForecast(ex, "São Paulo", "12345"));
        assertEquals(503, thrown.getStatusCode().value());
    }

    @Test
    void testRecoverGetWaveForecast() {
        Exception ex = new Exception("error");
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                weatherService.recoverGetWaveForecast(ex, "12345"));
        assertEquals(503, thrown.getStatusCode().value());
    }

    @Test
    void testRecoverDiscoverCityIdByName() {
        Exception ex = new Exception("error");
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () ->
                weatherService.recoverDiscoverCityIdByName(ex, "São Paulo"));
        assertEquals(503, thrown.getStatusCode().value());
    }
}
