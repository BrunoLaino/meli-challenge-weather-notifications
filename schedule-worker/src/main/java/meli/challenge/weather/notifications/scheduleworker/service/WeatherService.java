package meli.challenge.weather.notifications.scheduleworker.service;

import meli.challenge.weather.notifications.scheduleworker.feign.CptecFeignClient;
import meli.challenge.weather.notifications.scheduleworker.model.dto.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private final CptecFeignClient cptecFeignClient;

    public WeatherService(CptecFeignClient cptecFeignClient) {
        this.cptecFeignClient = cptecFeignClient;
    }

    @Cacheable(value = "weatherCache", key = "#cityName + '_' + (#cityId != null ? #cityId : '')")
    public CityWeatherForecast getForecast(String cityName, String cityId) {
        String finalCityId = cityId;
        if (finalCityId == null || finalCityId.isEmpty()) {
            finalCityId = discoverCityIdByName(cityName);
        }
        String xmlPrevisao = cptecFeignClient.getFourDayCityForecast(finalCityId);
        return parseXmlPrevisao(xmlPrevisao);
    }

    @Cacheable(value = "waveCache", key = "#cityId")
    public WaveForecast getWaveForecast(String cityId) {
        String xml = cptecFeignClient.getCityWaveForecastForDay(cityId, 0);
        return parseWaveXml(xml);
    }

    @Cacheable(value = "cityIdCache", key = "#cityName")
    public String discoverCityIdByName(String cityName) {
        String xml = cptecFeignClient.findCitiesByName(cityName);
        List<CityInfo> list = parseXmlListaCidades(xml);
        if (list.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma cidade encontrada para: " + cityName);
        }
        return list.get(0).getId();
    }

    private List<CityInfo> parseXmlListaCidades(String xml) {
        List<CityInfo> result = new ArrayList<>();
        try {
            Document doc = parseXml(xml);
            NodeList nodeList = doc.getElementsByTagName("cidade");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element el = (Element) nodeList.item(i);
                String nome = getText(el, "nome");
                String uf = getText(el, "uf");
                String id = getText(el, "id");
                CityInfo ci = new CityInfo();
                ci.setNome(nome);
                ci.setUf(uf);
                ci.setId(id);
                result.add(ci);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao parsear listaCidades", e);
        }
        return result;
    }

    private CityWeatherForecast parseXmlPrevisao(String xml) {
        CityWeatherForecast forecast = new CityWeatherForecast();
        try {
            Document doc = parseXml(xml);
            Element root = doc.getDocumentElement();

            forecast.setCityName(getText(root, "nome"));
            forecast.setUf(getText(root, "uf"));

            NodeList list = doc.getElementsByTagName("previsao");
            for (int i = 0; i < list.getLength(); i++) {
                Element el = (Element) list.item(i);
                String dia = getText(el, "dia");
                String tempo = getText(el, "tempo");
                String min = getText(el, "minima");
                String max = getText(el, "maxima");

                DailyForecast df = new DailyForecast();
                df.setDate(LocalDate.parse(dia));
                df.setWeather(tempo);
                df.setMinTemp(Integer.parseInt(min));
                df.setMaxTemp(Integer.parseInt(max));

                forecast.getDailyForecasts().add(df);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao parsear previsao.xml", e);
        }
        return forecast;
    }

    private WaveForecast parseWaveXml(String xml) {
        WaveForecast wf = new WaveForecast();
        try {
            Document doc = parseXml(xml);
            Element root = doc.getDocumentElement();

            wf.setCityName(getText(root, "nome"));

            if (!StringUtils.hasText(wf.getCityName()) || wf.getCityName().equalsIgnoreCase("undefined")) {
                return null;
            }

            wf.setUf(getText(root, "uf"));

            Element manhaEl = (Element) root.getElementsByTagName("manha").item(0);
            wf.setManha(parseWavePeriod(manhaEl));

            Element tardeEl = (Element) root.getElementsByTagName("tarde").item(0);
            wf.setTarde(parseWavePeriod(tardeEl));

            Element noiteEl = (Element) root.getElementsByTagName("noite").item(0);
            wf.setNoite(parseWavePeriod(noiteEl));

        } catch (Exception e) {
            throw new RuntimeException("Erro ao parsear ondas.xml", e);
        }
        return wf;
    }

    private WavePeriod parseWavePeriod(Element parent) {
        if (parent == null) return null;
        WavePeriod wp = new WavePeriod();
        wp.setAgitacao(getText(parent, "agitacao"));
        wp.setAltura(getText(parent, "altura"));
        wp.setDirecao(getText(parent, "direcao"));
        wp.setVento(getText(parent, "vento"));
        wp.setVentoDir(getText(parent, "vento_dir"));
        return wp;
    }

    private Document parseXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes(StandardCharsets.ISO_8859_1));
        return builder.parse(input);
    }

    private String getText(Element parent, String tagName) {
        NodeList nl = parent.getElementsByTagName(tagName);
        if (nl.getLength() > 0) {
            return nl.item(0).getTextContent();
        }
        return "";
    }
}
