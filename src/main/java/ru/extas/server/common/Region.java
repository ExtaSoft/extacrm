package ru.extas.server.common;

import lombok.*;

/**
 * Created by valery on 22.03.16.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    /**
     * Код ФИАС региона
     */
    private String fiasId;

    /**
     * Код КЛАДР региона
     */
    private String kladrId;

    /**
     * Регион с типом
     */
    private String nameWithType;

    /**
     * Тип региона (сокращенный).
     */
    private String type;

    /**
     * Тип региона.
     */
    private String typeFull;

    /**
     * Регион.
     */
    private String name;

    /**
     * Старое имя региона
     */
    private String oldName;

    /**
     * Столица региона
     */
    private String capital;
}
