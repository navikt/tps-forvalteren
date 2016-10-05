angular.module('tps-vedlikehold')
    .constant('responseFormConfig',
        
    {
        'FS03-FDNUMMER-PERSDATA-O': {
            rows: [
                [
                    {
                        fieldData: "fnr",
                        label: "Fødselsnummer",
                        flex: 30
                    },
                    {
                        fieldData: "kortnavn",
                        label: "Forkortet navn",
                        flex: 30
                    },
                    {
                        fieldData: "spesregType",
                        label: "SpesReg",
                        flex: 20
                    },
                    {
                        fieldData: "kodePersonStatus",
                        label: "Kode PS",
                        flex: 20
                    }
                ],
        
                [
                    {
                        fieldData: "fornavn",
                        label: "Fornavn",
                        flex: 30
                    },
                    {
                        fieldData: "mellomnavn",
                        label: "Mellomnavn",
                        flex: 30
                    },
                    {
                        fieldData: "etternavn",
                        label: "Etternavn",
                        flex: 40
                    }
                ],
        
                [
                    {
                        fieldData: "boAdresse1",
                        label: "Bostedsadresse 1",
                        flex: 30
                    },
                    {
                        fieldData: "boAdresse2",
                        label: "Bostedsadresse 2",
                        flex: 30
                    },
                    {
                        fieldData: "bolignr",
                        label: "Bolignr",
                        flex: 20
                    },
                    {
                        fieldData: "personStatus",
                        label: "Person Status",
                        flex: 20
                    }
                ],
        
                [
                    {
                        fieldData: "postnr",
                        label: "Postnr",
                        flex: 30
                    },
                    {
                        fieldData: "boPoststed",
                        label: "Poststed",
                        flex: 30
                    },
                    {
                        fieldData: "datoDo",
                        label: "Dødsdato",
                        flex: 20
                    },
                    {
                        fieldData: "datoUmyndiggjort",
                        label: "Dato Umyndig",
                        flex: 20
                    }
                ],
        
                [
                    {
                        fieldData: "postAdresse1",
                        label: "Post Adresse 1",
                        flex: 30
                    },
                    {
                        fieldData: "postAdresse1",
                        label: "Post Adresse 2",
                        flex: 30
                    },
                    {
                        fieldData: "postAdresse1",
                        label: "Post Adresse 3",
                        flex: 40
                    }
                ],
        
                [
                    {
                        fieldData: "tknr",
                        label: "Tknr",
                        flex: 15
                    },
                    {
                        fieldData: "tknrNavn",
                        label: "TkNavn",
                        flex: 15
                    },
                    {
                        fieldData: "kommunenr",
                        label: "Knr",
                        flex: 15
                    },
                    {
                        fieldData: "kommuneNavn",
                        label: "KomNavn",
                        flex: 15
                    },
                    {
                        fieldData: "tidligereKommunenr",
                        label: "Tidligere knr",
                        flex: 20
                    },
                    {
                        fieldData: "tidligereKNavn",
                        label: "Tidl.Komm.Navn",
                        flex: 20
                    }
                ],
        
                [
                    {
                        fieldData: "kodeSivilstand",
                        label: "Kode Sivilist",
                        flex: 15
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
                        fieldData: "kodeInnvandretFra",
                        label: "Kode Innvandret",
                        flex: 15
                    },
                    {
                        fieldData: "innvandretFra",
                        label: "Innvandret fra",
                        flex: 20
                    },
                    {
                        fieldData: "datoInnvandret",
                        label: "Dato Innvdr.",
                        flex: 10
                    },
                    {
                        fieldData: "datoFlyttet",
                        label: "Flytte Dato",
                        flex: 10
                    }
                ],
        
                [
                    {
                        fieldData: "kodeStatsborger",
                        label: "Kode statsb.",
                        flex: 15
                    },
                    {
                        fieldData: "statsborger",
                        label: "Statsborger",
                        flex: 15
                    },
                    {
                        fieldData: "datoStatsborger",
                        label: "Statsb. Dato",
                        flex: 15
                    },
                    {
                        fieldData: "kodeUtvandretTil",
                        label: "Kode Utv.til",
                        flex: 15
                    },
                    {
                        fieldData: "utvandretTil",
                        label: "Utvandret til",
                        flex: 20
                    },
                    {
                        fieldData: "datoUtvandret",
                        label: "Dato Utvandret",
                        flex: 20
                    }
                ],
        
                [
                    {
                        fieldData: "tlfPrivat_tlfNummer",
                        label: "Tlf privat",
                        flex: 15
                    },
                    {
                        fieldData: "tlfJobb_tlfNummer",
                        label: "Tlf jobb",
                        flex: 15
                    },
                    {
                        fieldData: "tlfMobil_tlfNummer",
                        label: "Tlf mobil",
                        flex: 15
                    },
                    {
                        fieldData: "epostAdresse",
                        label: "E-postadr",
                        flex: 15
                    },
                    {
                        fieldData: "giroNummer",
                        label: "Gironr",
                        flex: 20
                    },
                    {
                        fieldData: "kodeFodeland",
                        label: "Kode FødeLand",
                        flex: 10
                    },
                    {
                        fieldData: "fodeLand",
                        label: "Fødeland",
                        flex: 10
                    }
                ]
            ]
        },
        'FS03-FDNUMMER-KONTINFO-O': {
            rows: [
                [
                    {
                        fieldData: "fodselsnummer",
                        label: "Fødselsnummer",
                        flex: 30
                    },
                    {
                        fieldData: "kontoNummer",
                        label: "Kontonummer",
                        flex: 30
                    },
                    {
                        fieldData: "boAdresse1",
                        label: "Adresse 1",
                        flex: 20
                    },
                    {
                        fieldData: "bolignr",
                        label: "BoligNr",
                        flex: 10
                    }
                ],

                [
                    {
                        fieldData: "boPostnr",
                        label: "Postnummer",
                        flex: 20
                    },
                    {
                        fieldData: "tknr",
                        label: "tkNr",
                        flex: 5
                    },
                    {
                        fieldData: "tkNavn",
                        label: "tkNavn",
                        flex: 15
                    }
                ],
                [
                    {
                        fieldData: "kommuneNavn",
                        label: "KommuneNavn",
                        flex: 20
                    },
                    {
                        fieldData: "kommunenr",
                        label: "Kommunenummer",
                        flex: 10
                    },
                    {
                        fieldData: "land",
                        label: "Land",
                        flex: 20
                    },
                    {
                        fieldData: "adresseType",
                        label: "AdrType",
                        flex: 10
                    }
                ],

                [
                    {
                        fieldData: "kodeNAVenhet",
                        label: "KodeNAVenhet",
                        flex: 15
                    },
                    {
                        fieldData: "kodeNAVenhetBeskr",
                        label: "NAVenhet område",
                        flex: 15
                    },
                    {
                        fieldData: "NAVenhetSystem",
                        label: "NAVenhetSystem",
                        flex: 15
                    }
                ],

                [
                    {
                        fieldData: "adrSaksbehandler",
                        label: "AdrSaksbehandler",
                        flex: 15
                    },
                    {
                        fieldData: "adrTidspunktReg",
                        label: "adrTidspunktReg",
                        flex: 15
                    }
                ]
            ]
        },

        'FS03-NAADRSOK-PERSDATA-O': {
            rows: [
                [
                    {
                        fieldData: "fnr",
                        label: "Fødselsnummer",
                        flex: 30
                    },
                    {
                        fieldData: "fornavn",
                        label: "Fornavn",
                        flex: 30
                    },
                    {
                        fieldData: "mellomnavn",
                        label: "Mellomnavn",
                        flex: 20
                    },
                    {
                        fieldData: "etternavn",
                        label: "Etternavn",
                        flex: 10
                    }
                ],

                [
                    {
                        fieldData: "adresseType",
                        label: "adrType",
                        flex: 10
                    },
                    {
                        fieldData: "adresse1",
                        label: "Adr1",
                        flex: 5
                    },
                    {
                        fieldData: "adresse2",
                        label: "Adr2",
                        flex: 15
                    }
                ],
                [
                    {
                        fieldData: "kommuneNavn",
                        label: "KommuneNavn",
                        flex: 20
                    },
                    {
                        fieldData: "postnr",
                        label: "PostNr",
                        flex: 10
                    },
                    {
                        fieldData: "poststed",
                        label: "Poststed",
                        flex: 20
                    },
                    {
                        fieldData: "kommunenr",
                        label: "Kommunenr",
                        flex: 10
                    }
                ],

                [
                    {
                        fieldData: "tknr",
                        label: "TkNr",
                        flex: 15
                    },
                    {
                        fieldData: "personStatus",
                        label: "PersonStatus",
                        flex: 15
                    },
                    {
                        fieldData: "statsborgerskap",
                        label: "Statsborgerskap",
                        flex: 15
                    }
                ]
            ]
        }
    }
);
