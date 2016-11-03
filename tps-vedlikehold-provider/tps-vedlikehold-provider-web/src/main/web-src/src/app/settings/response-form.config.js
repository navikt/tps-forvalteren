angular.module('tps-vedlikehold')
    .constant('responseFormConfig',

        {
            'FS03-FDNUMMER-PERSDATA-O': {
                display: {
                    header: "${etternavn}, ${fornavn}",
                    properties: [
                        {
                            label: "Addresse",
                            template: "${boAdresse1}, ${postnr} ${boPoststed}",
                            flex: 30
                        },
                        {
                            label: "Fødselsnummer",
                            template: "${fnr}",
                            flex: 20
                        },
                        {
                            label: "TK-nummer",
                            template: "${tknr}",
                            flex: 20
                        },
                        {
                            label: "Kommune",
                            template: "${kommunenr}",
                            flex: 25
                        }
                    ]

                },
                rows: [
                    [
                        {
                            fieldData: "fnr",
                            label: "Fødselsnummer",
                            flex: 25
                        },
                        {
                            fieldData: "fnrTidspunkt",
                            label: "Fodselsnummer Tidspunkt",
                            flex: 25
                        },
                        {
                            fieldData: "fnrSystem",
                            label: "Fodselsnummer System",
                            flex: 20
                        },
                        {
                            fieldData: "fnrSaksbehandler",
                            label: "Fodselsnummer Saksbehandler",
                            flex: 20
                        }
                    ],
                    [
                        {
                            fieldData: "kortnavn",
                            label: "Forkortet navn",
                            flex: 25
                        },
                        {
                            fieldData: "fornavn",
                            label: "Fornavn",
                            flex: 25
                        },
                        {
                            fieldData: "mellomnavn",
                            label: "Mellomnavn",
                            flex: 20
                        },
                        {
                            fieldData: "etternavn",
                            label: "Mellomnavn",
                            flex: 20
                        }
                    ],

                    [
                        {
                            fieldData: "navnTidspunkt",
                            label: "Navn Tidsp.",
                            flex: 20
                        },
                        {
                            fieldData: "navnSystem",
                            label: "Navn System",
                            flex: 20
                        },
                        {
                            fieldData: "navnSaksbehandler",
                            label: "Navn Saksbehandler",
                            flex: 25
                        },
                        {
                            fieldData: "spesregType",
                            label: "Spesreg Type",
                            flex: 25
                        }
                    ],

                    [
                        {
                            fieldData: "boAdresse1",
                            label: "Bo-Adresse 1",
                            flex: 25
                        },
                        {
                            fieldData: "boAdresse2",
                            label: "Bo- Adresse 2",
                            flex: 25
                        },
                        {
                            fieldData: "postnr",
                            label: "Postnummer",
                            flex: 10
                        },
                        {
                            fieldData: "boPoststed",
                            label: "Bo-Poststed",
                            flex: 20
                        },
                        {
                            fieldData: "bolignr",
                            label: "Bolignummer",
                            flex: 10
                        }
                    ],

                    [
                        {
                            fieldData: "postAdresse1",
                            label: "Post-Adresse 1",
                            flex: 25
                        },
                        {
                            fieldData: "postAdresse2",
                            label: "Post-Adresse 2",
                            flex: 25
                        },
                        {
                            fieldData: "postAdresse3",
                            label: "Post-Adresse 3",
                            flex: 10
                        },
                        {
                            fieldData: "kommunenr",
                            label: "Kommunenr",
                            flex: 10
                        },
                        {
                            fieldData: "kommuneNavn",
                            label: "Kommune Navn",
                            flex: 20
                        }
                    ],

                    [
                        {
                            fieldData: "tknr",
                            label: "TK-Nummer",
                            flex: 10
                        },
                        {
                            fieldData: "tknrNavn",
                            label: "Tk Nr Navn",
                            flex: 25
                        },
                        {
                            fieldData: "tknrTidspunkt",
                            label: "Tk Nr. Tidspunkt",
                            flex: 10
                        },
                        {
                            fieldData: "tknrSystem",
                            label: "Tk Nr. System",
                            flex: 20
                        },
                        {
                            fieldData: "tknrSaksbehandler",
                            label: "Tk Nr. Saksbehandler",
                            flex: 25
                        }
                    ],
                    [
                        {
                            fieldData: "tidligereKommunenr",
                            label: "Tidligere KommuneNr",
                            flex: 15
                        },
                        {
                            fieldData: "tidligereKNavn",
                            label: "Tidligere KommuneNavn",
                            flex: 20
                        },
                        {
                            fieldData: "datoFlyttet",
                            label: "Dato flyttet",
                            flex: 15
                        },
                        {
                            fieldData: "kodePersonStatus",
                            label: "Person status",
                            flex: 10
                        },
                        {
                            fieldData: "personStatus",
                            label: "Person status",
                            flex: 30
                        }
                    ],

                    [
                        {
                            fieldData: "kodeFodeland",
                            label: "Kode Fodeland",
                            flex: 15
                        },
                        {
                            fieldData: "fodeLand",
                            label: "Fodeland",
                            flex: 20
                        },
                        {
                            fieldData: "kodeStatsborger",
                            label: "Kode statsborger",
                            flex: 15
                        },
                        {
                            fieldData: "statsborger",
                            label: "Statsborger",
                            flex: 20
                        },
                        {
                            fieldData: "datoStatsborger",
                            label: "Dato Statsborger",
                            flex: 20
                        }
                    ],
                    [
                        {
                            fieldData: "statsbTidspunkt",
                            label: "Stats B. Tidspunkt",
                            flex: 10
                        },
                        {
                            fieldData: "statsbSaksbehandler",
                            label: "Stats B Saksbehandler",
                            flex: 20
                        },
                        {
                            fieldData: "statsbSaksbehandler",
                            label: "StatsBSaksbehandler",
                            flex: 10
                        },
                        {
                            fieldData: "sivilstand",
                            label: "Sivilstand",
                            flex: 15
                        },
                        {
                            fieldData: "datoSivilstand",
                            label: "Dato Sivilstand",
                            flex: 15
                        },
                        {
                            fieldData: "datoDo",
                            label: "Dato Do.",
                            flex: 20
                        }
                    ],
                    [
                        {
                            fieldData: "kodeSivilstand",
                            label: "Kode Sivilstand",
                            flex: 10
                        },
                        {
                            fieldData: "sivilstand",
                            label: "Sivilstand",
                            flex: 25
                        },
                        {
                            fieldData: "datoSivilstand",
                            label: "Dato Sivilstand",
                            flex: 15
                        },
                        {
                            fieldData: "sivilstTidspunkt",
                            label: "Sivilst Tidspunkt",
                            flex: 15
                        },
                        {
                            fieldData: "sivilstSystem",
                            label: "sivilstSystem",
                            flex: 10
                        },
                        {
                            fieldData: "sivilstSaksbehandler",
                            label: "Sivil St. Saksbehandler",
                            flex: 15
                        }
                    ],

                    [
                        {
                            fieldData: "datoDo",
                            label: "Dato Do.",
                            flex: 20
                        },
                        {
                            fieldData: "doTidspunkt",
                            label: "Do Tidspunkt",
                            flex: 20
                        },
                        {
                            fieldData: "doSystem",
                            label: "Do System",
                            flex: 20
                        },
                        {
                            fieldData: "doSaksbehandler",
                            label: "Do Saksbehandler",
                            flex: 20
                        },
                        {
                            fieldData: "datoUmyndiggjort",
                            label: "Dato Umyndiggjort",
                            flex: 10
                        }
                    ],
                    [
                        {
                            fieldData: "kodeInnvandretFra",
                            label: "Kode innvandret Fra",
                            flex: 10
                        },
                        {
                            fieldData: "innvandretFra",
                            label: "Innvandret Fra",
                            flex: 25
                        },
                        {
                            fieldData: "datoInnvandret",
                            label: "Dato Innvandret",
                            flex: 15
                        },
                        {
                            fieldData: "kodeUtvandretTil",
                            label: "Kode Utvandret Til",
                            flex: 10
                        },
                        {
                            fieldData: "utvandretTil",
                            label: "Utvandret Til",
                            flex: 20
                        },
                        {
                            fieldData: "datoUtvandret",
                            label: "Dato Utvandret",
                            flex: 10
                        }
                    ],

                    [
                        {
                            fieldData: "giroNummer",
                            label: "Giro Nummer",
                            flex: 25
                        },
                        {
                            fieldData: "giroTidspunktReg",
                            label: "giro Tidsp. Reg.",
                            flex: 15
                        },
                        {
                            fieldData: "giroTidspunkt",
                            label: "giro Tidspunkt",
                            flex: 15
                        },
                        {
                            fieldData: "giroSystem",
                            label: "Giro system",
                            flex: 10
                        },
                        {
                            fieldData: "giroSaksbehandler",
                            label: "Giro Saksbehandler",
                            flex: 25
                        }
                    ],
                    [
                        {
                            fieldData: "tlfPrivat_tlfNummer",
                            label: "Tlf privat",
                            flex: 20
                        },
                        {
                            fieldData: "tlfPrivat_tlfTidspunkt",
                            label: "Tlf pr. tidspunkt",
                            flex: 20
                        },
                        {
                            fieldData: "tlfPrivat_tlfSystem",
                            label: "Tlf pr. System",
                            flex: 25
                        },
                        {
                            fieldData: "tlfPrivat_tlfSaksbehandler",
                            label: "Tlf pr. Saksbehandler",
                            flex: 25
                        }
                    ],
                    [
                        {
                            fieldData: "tlfJobb_tlfNummer",
                            label: "Tlf Jobb",
                            flex: 20
                        },
                        {
                            fieldData: "tlfJobb_tlfTidspunkt",
                            label: "Tlf jobb. tidspunkt",
                            flex: 20
                        },
                        {
                            fieldData: "tlfJobb_tlfSystem",
                            label: "Tlf jobb. System",
                            flex: 25
                        },
                        {
                            fieldData: "tlfJobb_tlfSaksbehandler",
                            label: "Tlf jobb. Saksbehandler",
                            flex: 25
                        }
                    ],
                    [
                        {
                            fieldData: "tlfMobil_tlfNummer",
                            label: "Tlf mobil",
                            flex: 25
                        },
                        {
                            fieldData: "tlfMobil_tlfTidspunkt",
                            label: "Tlf mob. Tidspunkt ",
                            flex: 25
                        },
                        {
                            fieldData: "tlfMobil_tlfSystem",
                            label: "Tlf mobil System",
                            flex: 20
                        },
                        {
                            fieldData: "tlfMobil_tlfSaksbehandler",
                            label: "Tlf mobil Saksbehandler",
                            flex: 20
                        }
                    ],
                    [
                        {
                            fieldData: "epostAdresse",
                            label: "Epostadresse",
                            flex: 25
                        },
                        {
                            fieldData: "epostTidspunkt",
                            label: "Eposttidspunkt ",
                            flex: 25
                        },
                        {
                            fieldData: "epostSystem",
                            label: "Epostsystem",
                            flex: 20
                        },
                        {
                            fieldData: "epostSaksbehandler",
                            label: "Epostsaksbehandler",
                            flex: 20
                        }
                    ]
                ]
            },
            'FS03-FDNUMMER-KONTINFO-O': {
                display: {
                    header: "${etternavn}, ${fornavn}",
                    properties: [
                        {
                            label: "Addresse",
                            template: "${boAdresse1}, ${postnr} ${boPoststed}",
                            flex: 30
                        },
                        {
                            label: "Fødselsnummer",
                            template: "${fodselsnummer}",
                            flex: 15
                        },
                        {
                            label: "TK-nummer",
                            template: "${tknr}",
                            flex: 15
                        },
                        {
                            label: "Kommune",
                            template: "${kommunenr}",
                            flex: 15
                        },
                        {
                            label: "NAV-enhet",
                            template: "${kodeNAVenhet} ${kodeNAVenhetBeskr}",
                            flex: 15
                        }
                    ]
                },
                rows: [
                    [
                        {
                            fieldData: "fodselsnummer",
                            label: "Fødselsnummer",
                            flex: 20
                        },
                        {
                            fieldData: "fodselsdato",
                            label: "Fødselsdato",
                            flex: 20
                        },
                        {
                            fieldData: "personstatus",
                            label: "Person status",
                            flex: 20
                        },
                        {
                            fieldData: "gjeldendePersonnavn",
                            label: "Gjeldende Personnavn",
                            flex: 20
                        },
                        {
                            fieldData: "kortnavn",
                            label: "Kortnavn",
                            flex: 20
                        }
                    ],

                    [
                        {
                            fieldData: "fornavn",
                            label: "Fornavn",
                            flex: 20
                        },
                        {
                            fieldData: "mellomnavn",
                            label: "Mellomnavn",
                            flex: 20
                        },
                        {
                            fieldData: "etternavn",
                            label: "Etternavn",
                            flex: 20
                        },
                        {
                            fieldData: "navnTidspunkt",
                            label: "Navn tidspunkt",
                            flex: 20
                        },
                        {
                            fieldData: "navnSystem",
                            label: "Navnsystem",
                            flex: 20
                        }
                    ],
                    [
                        {
                            fieldData: "navnSaksbehandler",
                            label: "Navn saksbehandler",
                            flex: 20
                        },
                        {
                            fieldData: "kontoNummer",
                            label: "Kontonummer",
                            flex: 20
                        },
                        {
                            fieldData: "banknavn",
                            label: "Banknavn",
                            flex: 20
                        },
                        {
                            fieldData: "regTidspunkt",
                            label: "Reg. Tidspunkt",
                            flex: 20
                        },
                        {
                            fieldData: "regSystem",
                            label: "Reg. System",
                            flex: 20
                        }
                    ],

                    [
                        {
                            fieldData: "regSaksbehandler",
                            label: "Reg. Saksbehandler",
                            flex: 20
                        },
                        {
                            fieldData: "boAdresse1",
                            label: "Bo-adresse 1",
                            flex: 20
                        },
                        {
                            fieldData: "boAdresse2",
                            label: "Bo-adresse 2",
                            flex: 20
                        },
                        {
                            fieldData: "boPostnr",
                            label: "Bo-postnummer",
                            flex: 20
                        },
                        {
                            fieldData: "boPoststed",
                            label: "Bo-poststed",
                            flex: 20
                        }
                    ],

                    [
                        {
                            fieldData: "datoFom",
                            label: "Bo-Dato Fra og Med",
                            flex: 20
                        },
                        {
                            fieldData: "datoTom",
                            label: "Bo-Dato Til og Med",
                            flex: 20
                        },
                        {
                            fieldData: "adresse1",
                            label: "Full Bo-adresse 2",
                            flex: 20
                        },
                        {
                            fieldData: "adresse2",
                            label: "Full Bo-adresse 2",
                            flex: 20
                        },
                        {
                            fieldData: "tilleggsAdresseSKD",
                            label: "Tilleggsadresse SKD",
                            flex: 20
                        }
                    ]
                ]
            },

            'FS03-NAADRSOK-PERSDATA-O': {
                display: {
                    header: "${etternavn}, ${fornavn}",
                    properties: [
                        {
                            label: "Addresse",
                            template: "${adresse1}, ${postnr} ${poststed}",
                            flex: 30
                        },
                        {
                            label: "Fødselsnummer",
                            template: "${fnr}",
                            flex: 20
                        },
                        {
                            label: "TK-nummer",
                            template: "${tknr}",
                            flex: 20
                        },
                        {
                            label: "Kommune",
                            template: "${kommunenr}",
                            flex: 20
                        }
                    ]
                },
                rows: [
                    [
                        {
                            fieldData: "fnr",
                            label: "Fødselsnummer",
                            flex: 25
                        },
                        {
                            fieldData: "fornavn",
                            label: "Fornavn",
                            flex: 25
                        },
                        {
                            fieldData: "mellomnavn",
                            label: "Mellomnavn",
                            flex: 20
                        },
                        {
                            fieldData: "etternavn",
                            label: "Etternavn",
                            flex: 20
                        }
                    ],

                    [
                        {
                            fieldData: "adresseType",
                            label: "Adresse type",
                            flex: 25
                        },
                        {
                            fieldData: "adresse1",
                            label: "Adresse 1",
                            flex: 25
                        },
                        {
                            fieldData: "adresse2",
                            label: "Adresse 2",
                            flex: 20
                        },
                        {
                            fieldData: "adresse3",
                            label: "Adresse 3",
                            flex: 20
                        }
                    ],
                    [
                        {
                            fieldData: "landKode",
                            label: "Adresse landkode",
                            flex: 25
                        },
                        {
                            fieldData: "kommunenr",
                            label: "Kommunenr",
                            flex: 25
                        },
                        {
                            fieldData: "poststed",
                            label: "Poststed",
                            flex: 20
                        },
                        {
                            fieldData: "postnr",
                            label: "Postnummer",
                            flex: 20
                        }
                    ],

                    [
                        {
                            fieldData: "statsborgerskap",
                            label: "Statsborgerskap",
                            flex: 25
                        },
                        {
                            fieldData: "kjonn",
                            label: "Kjonn",
                            flex: 25
                        },
                        {
                            fieldData: "tknr",
                            label: "TkNr",
                            flex: 20
                        },
                        {
                            fieldData: "personStatus",
                            label: "PersonStatus",
                            flex: 20
                        }
                    ]
                ]
            }
        }
    );
