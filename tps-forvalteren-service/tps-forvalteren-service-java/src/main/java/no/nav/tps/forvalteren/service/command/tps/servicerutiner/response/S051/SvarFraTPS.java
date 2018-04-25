
package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Svarstruktur fra TPS.
 * 
 * <p>Java class for SvarFraTPS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SvarFraTPS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="svarStatus" type="{http://www.rtv.no/NamespaceTPS}StatusFraTPS"/>
 *         &lt;choice>
 *           &lt;element name="ingenReturData">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="adresseDataS051" type="{http://www.rtv.no/NamespaceTPS}AdresseDataFraTpsS051"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SvarFraTPS", namespace = "http://www.rtv.no/NamespaceTPS", propOrder = {
    "svarStatus",
    "ingenReturData",
    "adresseDataS051"
})
public class SvarFraTPS {

    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
    protected StatusFraTPS svarStatus;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected SvarFraTPS.IngenReturData ingenReturData;
    @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS")
    protected AdresseDataFraTpsS051 adresseDataS051;

    /**
     * Gets the value of the svarStatus property.
     *
     * @return
     *     possible object is
     *     {@link StatusFraTPS }
     *
     */
    public StatusFraTPS getSvarStatus() {
        return svarStatus;
    }

    /**
     * Sets the value of the svarStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link StatusFraTPS }
     *
     */
    public void setSvarStatus(StatusFraTPS value) {
        this.svarStatus = value;
    }

    /**
     * Gets the value of the ingenReturData property.
     *
     * @return
     *     possible object is
     *     {@link SvarFraTPS.IngenReturData }
     *
     */
    public SvarFraTPS.IngenReturData getIngenReturData() {
        return ingenReturData;
    }

    /**
     * Sets the value of the ingenReturData property.
     *
     * @param value
     *     allowed object is
     *     {@link SvarFraTPS.IngenReturData }
     *
     */
    public void setIngenReturData(SvarFraTPS.IngenReturData value) {
        this.ingenReturData = value;
    }

    /**
     * Gets the value of the adresseDataS051 property.
     * 
     * @return
     *     possible object is
     *     {@link AdresseDataFraTpsS051 }
     *     
     */
    public AdresseDataFraTpsS051 getAdresseDataS051() {
        return adresseDataS051;
    }

    /**
     * Sets the value of the adresseDataS051 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdresseDataFraTpsS051 }
     *     
     */
    public void setAdresseDataS051(AdresseDataFraTpsS051 value) {
        this.adresseDataS051 = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class IngenReturData {


    }

}
