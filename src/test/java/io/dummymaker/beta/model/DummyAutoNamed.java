package io.dummymaker.beta.model;

import java.util.List;

/**
 * ! NO DESCRIPTION !
 *
 * @author GoodforGod
 * @since 15.09.2019
 */
public class DummyAutoNamed {

    private String code;
    private String name;
    private String surname;

    private List<String> surnames;
    private List<String> btcHexes;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<String> getSurnames() {
        return surnames;
    }

    public List<String> getBtcHexes() {
        return btcHexes;
    }
}
