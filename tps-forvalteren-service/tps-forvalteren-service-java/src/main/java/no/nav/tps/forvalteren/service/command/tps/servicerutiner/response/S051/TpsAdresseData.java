
package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tpsServiceRutine" type="{http://www.rtv.no/NamespaceTPS}tpsSporsmal"/>
 *         &lt;element name="tpsSvar" type="{http://www.rtv.no/NamespaceTPS}SvarFraTPS" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tpsServiceRutine",
    "tpsSvar"
})
@XmlRootElement(name = "tpsAdresseData", namespace = "http://www.rtv.no/NamespaceTPS")
public class TpsAdresseData {

    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
    protected TpsSporsmal tpsServiceRutine;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected SvarFraTPS tpsSvar;

    /**
     * Gets the value of the tpsServiceRutine property.
     * 
     * @return
     *     possible object is
     *     {@link TpsSporsmal }
     *     
     */
    public TpsSporsmal getTpsServiceRutine() {
        return tpsServiceRutine;
    }

    /**
     * Sets the value of the tpsServiceRutine property.
     * 
     * @param value
     *     allowed object is
     *     {@link TpsSporsmal }
     *     
     */
    public void setTpsServiceRutine(TpsSporsmal value) {
        this.tpsServiceRutine = value;
    }

    /**
     * Gets the value of the tpsSvar property.
     * 
     * @return
     *     possible object is
     *     {@link SvarFraTPS }
     *     
     */
    public SvarFraTPS getTpsSvar() {
        return tpsSvar;
    }

    /**
     * Sets the value of the tpsSvar property.
     * 
     * @param value
     *     allowed object is
     *     {@link SvarFraTPS }
     *     
     */
    public void setTpsSvar(SvarFraTPS value) {
        this.tpsSvar = value;
    }

}
