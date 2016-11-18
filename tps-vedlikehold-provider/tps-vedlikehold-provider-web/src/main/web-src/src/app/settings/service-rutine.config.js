angular.module('tps-vedlikehold')
    .constant('serviceRutineConfig',

    {
        'FS03-FDNUMMER-PERSDATA-O': {
            'nonUniquePropertiesContainer': [
                'tlfPrivat',
                'tlfJobb',
                'tlfMobil'
            ]
        },

        'FS03-FDNUMMER-KONTINFO-O': {
            'nonUniquePropertiesContainer': [
                'tlfPrivat',
                'tlfJobb',
                'tlfMobil'
            ]
        },
        'FS03-NAADRSOK-PERSDATA-O': {
        },
        "FS03-FDNUMMER-GIRONUMR-O": {
        },
        "FS03-FDNUMMER-ADRESSER-O": {
            'nonUniquePropertiesContainer': [
                'datoFrom',
                'datoTom',
                'landKode',
                "land",
                "adresseType",
                "beskrAdrType",
                "adresse1",
                "adresse2",
                "adresse3",
                "postnr",
                "poststed",
                "adrTidspunktReg",
                "adrSystem",
                "adrSaksbehandler"
            ]
        },
        'FS03-OTILGANG-TILSRTPS-O': {
        },
        'EndreNavn': {
        }



    }
);
