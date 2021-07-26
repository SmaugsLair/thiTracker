package com.smaugslair.thitracker.data;


public interface ThiEntity {

    public <T extends Number> T getId() ;

    public <S extends ThiEntity> S createEmptyObject();

}
