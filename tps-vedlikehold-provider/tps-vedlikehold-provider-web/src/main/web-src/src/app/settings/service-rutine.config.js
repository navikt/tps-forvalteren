angular.module('tps-vedlikehold')
    .constant('serviceRutineConfig',

    {
        'FS03-FDNUMMER-PERSDATA-O': {
            'serviceRutineFieldsTemplate': [
                'fnr',
                'aksjonsDato'
            ],
            'serviceRutineReturnedDataLabel': 'personDataS004',
            'nonUniquePropertiesContainer': [
                'tlfPrivat',
                'tlfJobb',
                'tlfMobil'
            ]
        },
        'FS03-OTILGANG-TILSRTPS-O': {
            'serviceRutineFieldsTemplate': [],
            'serviceRutineReturnedDataLabel': 'ingenReturData',
            'nonUniquePropertiesContainer': []
        }
    }
);
