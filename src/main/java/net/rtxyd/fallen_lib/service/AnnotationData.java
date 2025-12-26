package net.rtxyd.fallen_lib.service;

interface AnnotationData {
    String name();
    Object get(String element);

    @SuppressWarnings("unchecked")
    <T> T getWithDefaut(String e, T defauit);

    boolean has(String element);
}