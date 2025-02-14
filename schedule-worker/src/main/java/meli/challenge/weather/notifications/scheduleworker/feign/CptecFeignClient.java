package meli.challenge.weather.notifications.scheduleworker.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cptecClient", url = "http://servicos.cptec.inpe.br/XML")
public interface CptecFeignClient {

    @GetMapping("/listaCidades")
    String findCitiesByName(@RequestParam("city") String cityName);

    String getFourDayCityForecast(@PathVariable("idCidade") String cityId);

    @GetMapping("/cidade/{idCidade}/dia/{dia}/ondas.xml")
    String getCityWaveForecastForDay(@PathVariable("idCidade") String cityId,
                                     @PathVariable("dia") int day);

}
