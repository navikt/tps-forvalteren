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
                'aksjonsKode'
            ],
            'serviceRutineReturnedDataLabel': 'personDataS050',
            'nonUniquePropertiesContainer': [

            ]
        },
        'FS03-OTILGANG-TILSRTPS-O': {
            'serviceRutineFieldsOrderTemplate': [],
            'serviceRutineReturnedDataLabel': 'ingenReturData',
            'nonUniquePropertiesContainer': []
        }
    }
);
