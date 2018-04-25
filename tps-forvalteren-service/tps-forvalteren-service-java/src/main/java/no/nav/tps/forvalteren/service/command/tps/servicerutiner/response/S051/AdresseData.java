
package no.nav.tps.forvalteren.service.command.tps.servicerutiner.response.S051;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Typen beskriver alle feltene returnert fra S051
 * 
 * <p>Java class for adresseData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="adresseData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.rtv.no/NamespaceTPS}funneAdressedata">
 *       &lt;sequence>
 *         &lt;element name="adrData">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="knr" type="{http://www.rtv.no/NamespaceTPS}Tkommunenr"/>
 *                   &lt;element name="knavn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="adrnavn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="husnrfra" type="{http://www.rtv.no/NamespaceTPS}Thusnr"/>
 *                   &lt;element name="husnrtil" type="{http://www.rtv.no/NamespaceTPS}Thusnr"/>
 *                   &lt;element name="pnr" type="{http://www.rtv.no/NamespaceTPS}Tpostnr"/>
 *                   &lt;element name="psted" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="geotilk" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="gkode" type="{http://www.rtv.no/NamespaceTPS}Tgatekode"/>
 *                   &lt;element name="bydel" type="{http://www.rtv.no/NamespaceTPS}Tbydel"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "adresseData", namespace = "http://www.rtv.no/NamespaceTPS", propOrder = {
        "knr",
        "knavn",
        "adrnavn",
        "husnrfra",
        "husnrtil",
        "pnr",
        "psted",
        "geotilk",
        "gkode",
        "bydel"
})
public class AdresseData
    extends FunneAdressedata
{

   


        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String knr;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String knavn;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String adrnavn;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String husnrfra;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String husnrtil;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String pnr;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String psted;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String geotilk;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String gkode;
        @XmlElement(namespace = "http://www.rtv.no/NamespaceTPS", required = true)
        protected String bydel;

        /**
         * Gets the value of the knr property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKnr() {
            return knr;
        }

        /**
         * Sets the value of the knr property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKnr(String value) {
            this.knr = value;
        }

        /**
         * Gets the value of the knavn property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getKnavn() {
            return knavn;
        }

        /**
         * Sets the value of the knavn property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setKnavn(String value) {
            this.knavn = value;
        }

        /**
         * Gets the value of the adrnavn property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAdrnavn() {
            return adrnavn;
        }

        /**
         * Sets the value of the adrnavn property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAdrnavn(String value) {
            this.adrnavn = value;
        }

        /**
         * Gets the value of the husnrfra property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHusnrfra() {
            return husnrfra;
        }

        /**
         * Sets the value of the husnrfra property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHusnrfra(String value) {
            this.husnrfra = value;
        }

        /**
         * Gets the value of the husnrtil property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHusnrtil() {
            return husnrtil;
        }

        /**
         * Sets the value of the husnrtil property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHusnrtil(String value) {
            this.husnrtil = value;
        }

        /**
         * Gets the value of the pnr property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPnr() {
            return pnr;
        }

        /**
         * Sets the value of the pnr property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPnr(String value) {
            this.pnr = value;
        }

        /**
         * Gets the value of the psted property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPsted() {
            return psted;
        }

        /**
         * Sets the value of the psted property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPsted(String value) {
            this.psted = value;
        }

        /**
         * Gets the value of the geotilk property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGeotilk() {
            return geotilk;
        }

        /**
         * Sets the value of the geotilk property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGeotilk(String value) {
            this.geotilk = value;
        }

        /**
         * Gets the value of the gkode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getGkode() {
            return gkode;
        }

        /**
         * Sets the value of the gkode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setGkode(String value) {
            this.gkode = value;
        }

        /**
         * Gets the value of the bydel property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBydel() {
            return bydel;
        }

        /**
         * Sets the value of the bydel property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBydel(String value) {
            this.bydel = value;
        }

    

}
