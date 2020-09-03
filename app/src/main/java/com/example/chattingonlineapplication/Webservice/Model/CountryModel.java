package com.example.chattingonlineapplication.Webservice.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryModel implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("topLevelDomain")
    private String[] topLevelDomain;

    @JsonProperty("alpha2Code")
    private String alpha2Code;

    @JsonProperty("alpha3Code")
    private String alpha3Code;

    @JsonProperty("callingCodes")
    private String[] callingCodes;

    @JsonProperty("capital")
    private String capital;

    @JsonProperty("altSpellings")
    private String[] altSpellings;

    @JsonProperty("region")
    private String region;

    @JsonProperty("subregion")
    private String subregion;

    @JsonProperty("population")
    private int population;

    @JsonProperty("latlng")
    private int[] latlng;

    @JsonProperty("demonym")
    private String demonym;

    @JsonProperty("area")
    private int area;

    @JsonProperty("gini")
    private int gini;

    @JsonProperty("timezones")
    private String[] timezones;

    @JsonProperty("borders")
    private String[] borders;

    @JsonProperty("nativeName")
    private String nativeName;

    @JsonProperty("currenencies")
    private Object[] currenencies;

    @JsonProperty("languages")
    private Object[] languages;

    @JsonProperty("translations")
    private Object[] translations;

    @JsonProperty("flag")
    private String flag;

    @JsonProperty("regionalBlocs")
    private Object[] regionalBlocs;

    @JsonProperty("cioc")
    private String cioc;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("topLevelDomain")
    public String[] getTopLevelDomain() {
        return topLevelDomain;
    }

    @JsonProperty("topLevelDomain")
    public void setTopLevelDomain(String[] topLevelDomain) {
        this.topLevelDomain = topLevelDomain;
    }

    @JsonProperty("alpha2Code")
    public String getAlpha2Code() {
        return alpha2Code;
    }

    @JsonProperty("alpha2Code")
    public void setAlpha2Code(String alpha2Code) {
        this.alpha2Code = alpha2Code;
    }

    @JsonProperty("alpha3Code")
    public String getAlpha3Code() {
        return alpha3Code;
    }

    @JsonProperty("alpha3Code")
    public void setAlpha3Code(String alpha3Code) {
        this.alpha3Code = alpha3Code;
    }

    @JsonProperty("callingCodes")
    public String[] getCallingCodes() {
        return callingCodes;
    }

    @JsonProperty("callingCodes")
    public void setCallingCodes(String[] callingCodes) {
        this.callingCodes = callingCodes;
    }

    @JsonProperty("capital")
    public String getCapital() {
        return capital;
    }

    @JsonProperty("capital")
    public void setCapital(String capital) {
        this.capital = capital;
    }

    @JsonProperty("altSpellings")
    public String[] getAltSpellings() {
        return altSpellings;
    }

    @JsonProperty("altSpellings")
    public void setAltSpellings(String[] altSpellings) {
        this.altSpellings = altSpellings;
    }

    @JsonProperty("region")
    public String getRegion() {
        return region;
    }

    @JsonProperty("region")
    public void setRegion(String region) {
        this.region = region;
    }

    @JsonProperty("subregion")
    public String getSubregion() {
        return subregion;
    }

    @JsonProperty("subregion")
    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    @JsonProperty("population")
    public int getPopulation() {
        return population;
    }

    @JsonProperty("population")
    public void setPopulation(int population) {
        this.population = population;
    }

    @JsonProperty("latlng")
    public int[] getLatlng() {
        return latlng;
    }

    @JsonProperty("latlng")
    public void setLatlng(int[] latlng) {
        this.latlng = latlng;
    }

    @JsonProperty("demonym")
    public String getDemonym() {
        return demonym;
    }

    @JsonProperty("demonym")
    public void setDemonym(String demonym) {
        this.demonym = demonym;
    }

    @JsonProperty("area")
    public int getArea() {
        return area;
    }

    @JsonProperty("area")
    public void setArea(int area) {
        this.area = area;
    }

    @JsonProperty("gini")
    public int getGini() {
        return gini;
    }

    @JsonProperty("gini")
    public void setGini(int gini) {
        this.gini = gini;
    }

    @JsonProperty("timezones")
    public String[] getTimezones() {
        return timezones;
    }

    @JsonProperty("timezones")
    public void setTimezones(String[] timezones) {
        this.timezones = timezones;
    }

    @JsonProperty("borders")
    public String[] getBorders() {
        return borders;
    }

    @JsonProperty("borders")
    public void setBorders(String[] borders) {
        this.borders = borders;
    }

    @JsonProperty("nativeName")
    public String getNativeName() {
        return nativeName;
    }

    @JsonProperty("nativeName")
    public void setNativeName(String navitveName) {
        this.nativeName = navitveName;
    }

    @JsonProperty("currenencies")
    public Object[] getCurrenencies() {
        return currenencies;
    }

    @JsonProperty("currenencies")
    public void setCurrenencies(Object[] currenencies) {
        this.currenencies = currenencies;
    }

    @JsonProperty("languages")
    public Object[] getLanguages() {
        return languages;
    }

    @JsonProperty("languages")
    public void setLanguages(Object[] languages) {
        this.languages = languages;
    }

    @JsonProperty("translations")
    public Object[] getTranslations() {
        return translations;
    }

    @JsonProperty("translations")
    public void setTranslations(Object[] translations) {
        this.translations = translations;
    }

    @JsonProperty("flag")
    public String getFlag() {
        return flag;
    }

    @JsonProperty("flag")
    public void setFlag(String flag) {
        this.flag = flag;
    }

    @JsonProperty("regionalBlocs")
    public Object[] getRegionalBlocs() {
        return regionalBlocs;
    }

    @JsonProperty("regionalBlocs")
    public void setRegionalBlocs(Object[] regionalBlocs) {
        this.regionalBlocs = regionalBlocs;
    }

    @JsonProperty("cioc")
    public String getCioc() {
        return cioc;
    }

    @JsonProperty("cioc")
    public void setCioc(String cioc) {
        this.cioc = cioc;
    }
}
