
package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * RequestTypene mot TPS
 * 
 * <p>Java class for tpsSporsmal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tpsSporsmal">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="serviceRutinenavn">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.rtv.no/NamespaceTPS}SRnavn">
 *                 &lt;whiteSpace value="collapse"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="aksjonsKode">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.rtv.no/NamespaceTPS}AK">
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="aksjonsKode2">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.rtv.no/NamespaceTPS}AK2">
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="adresseNavnsok">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="typesok">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;maxLength value="1"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="kommuneNrsok">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *                 &lt;maxLength value="4"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="postNrsok">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *                 &lt;maxLength value="4"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="husNrsok">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *                 &lt;maxLength value="4"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="sortering">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *                 &lt;maxLength value="1"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="alleSkrivevarianter">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *                 &lt;maxLength value="1"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="visPostnr">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *                 &lt;maxLength value="1"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="maxRetur">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                 &lt;whiteSpace value="collapse"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="alltidRetur">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;whiteSpace value="collapse"/>
 *                 &lt;maxLength value="1"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *         &lt;/sequence>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tpsSporsmal", namespace = "http://www.rtv.no/NamespaceTPS", propOrder = {
    "serviceRutinenavn",
    "aksjonsKode",
    "aksjonsKode2",
    "adresseNavnsok",
    "typesok",
    "kommuneNrsok",
    "postNrsok",
    "husNrsok",
    "sortering",
    "alleSkrivevarianter",
    "visPostnr",
    "maxRetur",
    "alltidRetur"
})
public class TpsSporsmal {

    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected SRnavn serviceRutinenavn;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String aksjonsKode;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String aksjonsKode2;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String adresseNavnsok;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String typesok;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String kommuneNrsok;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String postNrsok;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String husNrsok;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String sortering;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String alleSkrivevarianter;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String visPostnr;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected Integer maxRetur;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected String alltidRetur;

    /**
     * Gets the value of the serviceRutinenavn property.
     * 
     * @return
     *     possible object is
     *     {@link SRnavn }
     *     
     */
    public SRnavn getServiceRutinenavn() {
        return serviceRutinenavn;
    }

    /**
     * Sets the value of the serviceRutinenavn property.
     * 
     * @param value
     *     allowed object is
     *     {@link SRnavn }
     *     
     */
    public void setServiceRutinenavn(SRnavn value) {
        this.serviceRutinenavn = value;
    }

    /**
     * Gets the value of the aksjonsKode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAksjonsKode() {
        return aksjonsKode;
    }

    /**
     * Sets the value of the aksjonsKode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAksjonsKode(String value) {
        this.aksjonsKode = value;
    }

    /**
     * Gets the value of the aksjonsKode2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAksjonsKode2() {
        return aksjonsKode2;
    }

    /**
     * Sets the value of the aksjonsKode2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAksjonsKode2(String value) {
        this.aksjonsKode2 = value;
    }

    /**
     * Gets the value of the adresseNavnsok property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresseNavnsok() {
        return adresseNavnsok;
    }

    /**
     * Sets the value of the adresseNavnsok property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresseNavnsok(String value) {
        this.adresseNavnsok = value;
    }

    /**
     * Gets the value of the typesok property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypesok() {
        return typesok;
    }

    /**
     * Sets the value of the typesok property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypesok(String value) {
        this.typesok = value;
    }

    /**
     * Gets the value of the kommuneNrsok property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKommuneNrsok() {
        return kommuneNrsok;
    }

    /**
     * Sets the value of the kommuneNrsok property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKommuneNrsok(String value) {
        this.kommuneNrsok = value;
    }

    /**
     * Gets the value of the postNrsok property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostNrsok() {
        return postNrsok;
    }

    /**
     * Sets the value of the postNrsok property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostNrsok(String value) {
        this.postNrsok = value;
    }

    /**
     * Gets the value of the husNrsok property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHusNrsok() {
        return husNrsok;
    }

    /**
     * Sets the value of the husNrsok property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHusNrsok(String value) {
        this.husNrsok = value;
    }

    /**
     * Gets the value of the sortering property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSortering() {
        return sortering;
    }

    /**
     * Sets the value of the sortering property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSortering(String value) {
        this.sortering = value;
    }

    /**
     * Gets the value of the alleSkrivevarianter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlleSkrivevarianter() {
        return alleSkrivevarianter;
    }

    /**
     * Sets the value of the alleSkrivevarianter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlleSkrivevarianter(String value) {
        this.alleSkrivevarianter = value;
    }

    /**
     * Gets the value of the visPostnr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVisPostnr() {
        return visPostnr;
    }

    /**
     * Sets the value of the visPostnr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVisPostnr(String value) {
        this.visPostnr = value;
    }

    /**
     * Gets the value of the maxRetur property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMaxRetur() {
        return maxRetur;
    }

    /**
     * Sets the value of the maxRetur property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxRetur(Integer value) {
        this.maxRetur = value;
    }

    /**
     * Gets the value of the alltidRetur property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlltidRetur() {
        return alltidRetur;
    }

    /**
     * Sets the value of the alltidRetur property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlltidRetur(String value) {
        this.alltidRetur = value;
    }

}
