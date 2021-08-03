package com.smaugslair.thitracker.data;


public interface ThiEntity {

    <T extends Number> T getId() ;

    <S extends ThiEntity> S createEmptyObject();

}
