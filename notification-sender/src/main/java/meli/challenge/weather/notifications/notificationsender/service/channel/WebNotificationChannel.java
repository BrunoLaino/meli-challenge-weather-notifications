package meli.challenge.weather.notifications.notificationsender.service.channel;

import lombok.RequiredArgsConstructor;
import meli.challenge.weather.notifications.notificationsender.model.dto.NotificationMessageDTO;
import meli.challenge.weather.notifications.notificationsender.model.dto.WavePeriodDTO;
import meli.challenge.weather.notifications.notificationsender.service.WebSocketNotificationService;
import org.springframework.stereotype.Component;

/**
 * "Envio" para aplicação Web (neste desafio,
 * significa apenas salvar a notificação na tabela 'notifications')
 */
@Component
@RequiredArgsConstructor
public class WebNotificationChannel implements NotificationChannel {

    private final WebSocketNotificationService webSocketNotificationService;

    @Override
    public boolean supports(NotificationMessageDTO message) {
        return true;
    }

    @Override
    public void send(NotificationMessageDTO message) {
        String finalText = buildNotificationText(message);

        webSocketNotificationService.sendNotification(finalText);

        System.out.println("[WEB] Notificação salva: userId="
                + message.getUserId()
                + " -> " + finalText);
    }

    private String buildNotificationText(NotificationMessageDTO msg) {
        StringBuilder sb = new StringBuilder();

        if (msg.getForecast() != null) {
            sb.append("Previsão para cidade: ")
                    .append(msg.getForecast().getCityName())
                    .append(" (").append(msg.getForecast().getUf()).append(")\n");

            if (msg.getForecast().getDailyForecastDTOS() != null) {
                sb.append("Próximos dias:\n");
                msg.getForecast().getDailyForecastDTOS().forEach(df -> sb.append(" - ")
                        .append(df.getDate())
                        .append(": Mín=").append(df.getMinTemp())
                        .append("°C, Máx=").append(df.getMaxTemp())
                        .append("°C, Tempo=").append(df.getWeather())
                        .append("\n"));
            }
        }

        if (msg.getWaveForecastDTO() != null) {
            sb.append("\nPrevisão de Ondas para hoje:\n");
            sb.append("Manhã: ").append(buildWavePeriod(msg.getWaveForecastDTO().getManha())).append("\n");
            sb.append("Tarde: ").append(buildWavePeriod(msg.getWaveForecastDTO().getTarde())).append("\n");
            sb.append("Noite: ").append(buildWavePeriod(msg.getWaveForecastDTO().getNoite())).append("\n");
        }

        return sb.toString();
    }

    private String buildWavePeriod(WavePeriodDTO wavePeriodDTO) {
        if (wavePeriodDTO == null) {
            return "Não disponível.";
        }
        return String.format("Agitação: %s, Altura: %s, Direção: %s, Vento: %s (%s)",
                wavePeriodDTO.getAgitacao(),
                wavePeriodDTO.getAltura(),
                wavePeriodDTO.getDirecao(),
                wavePeriodDTO.getVento(),
                wavePeriodDTO.getVentoDir());
    }
}