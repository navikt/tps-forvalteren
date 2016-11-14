angular.module('tps-vedlikehold')
    .constant('serviceRutineConfig',

    {
        'FS03-FDNUMMER-PERSDATA-O': {
            'serviceRutineFieldsOrderTemplate': [
                'fnr',
                'aksjonsDato',
                'aksjonsKode'
            ],
            'serviceRutineReturnedDataLabel': 'personDataS004',
            'nonUniquePropertiesContainer': [
                'tlfPrivat',
                'tlfJobb',
                'tlfMobil'
            ]
        },

        'FS03-FDNUMMER-KONTINFO-O': {
            'serviceRutineFieldsOrderTemplate': [
                'fnr',
                'aksjonsDato',
                'aksjonsKode'
            ],
            'serviceRutineReturnedDataLabel': 'personDataS600',
            'nonUniquePropertiesContainer': [
                'tlfPrivat',
                'tlfJobb',
                'tlfMobil'
            ]
        },
        'FS03-NAADRSOK-PERSDATA-O': {
            'serviceRutineFieldsOrderTemplate': [
                'navn',
                'fornavn',
                'etternavn',
                'aksjonsDato',
                'aksjonsKode',
                'adresseNavn',
                'postnr',
                'husnrFra',
                'husnrTil',
                'knr'
            ],
            'serviceRutineReturnedDataLabel': 'personDataS050',
            'nonUniquePropertiesContainer': [

            ]
        },
        "FS03-FDNUMMER-GIRONUMR-O": {
            'serviceRutineReturnedDataLabel': 'personDataS102',
            'nonUniquePropertiesContainer': [

            ]
        },
        'FS03-OTILGANG-TILSRTPS-O': {
            'serviceRutineFieldsOrderTemplate': [],
            'serviceRutineReturnedDataLabel': 'ingenReturData',
            'nonUniquePropertiesContainer': []
        },
        'EndreNavn': {
            'serviceRutineFieldsOrderTemplate': [
                'offentligIdent',
                'fornavn',
                'mellomnavn',
                'etternavn',
                'kilde'
            ]
        }



    }
);
