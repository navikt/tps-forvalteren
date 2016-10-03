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
        'FS03-OTILGANG-TILSRTPS-O': {
            'serviceRutineFieldsOrderTemplate': [],
            'serviceRutineReturnedDataLabel': 'ingenReturData',
            'nonUniquePropertiesContainer': []
        }
    }
);