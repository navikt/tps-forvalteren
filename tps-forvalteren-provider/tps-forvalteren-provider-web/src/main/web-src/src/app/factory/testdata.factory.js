angular.module('tps-forvalteren.factory')
    .factory('testdataFactory', ['testdataService', function (testdataService) {
        var testdataFactory = {};

        var gruppeData = {};
        var currentGruppeId;
        var isLoadSuccess = false;
        var errorMsg;

        testdataFactory.getIsLoadSuccess = function () {
            return isLoadSuccess;
        };

        testdataFactory.getGruppe = function (gruppeId) {
            if (gruppeId !== currentGruppeId) {
                loadGruppe(gruppeId);
                currentGruppeId = gruppeId;
            }

            return gruppeData;
        };

        function loadGruppe (gruppeId) {
            testdataService.getTestpersoner(gruppeId).then(
                function (result) {
                    gruppeData = result.data;
                    isLoadSuccess = true;
                },
                function (error) {
                    errorMsg = error;
                    isLoadSuccess = false;
                }
            );
        }

        return testdataFactory;
    }]);