angular.module('tps-forvalteren.service')
    .service('doedsmeldingerService', ['$http', function($http){

        var self = this;
        var url = 'api/v1/doedsmelding';

        // Sjekke om identen er allerede død.
        self.getIsDead = function() {

        };

        // Lag en dødsmelding på ident.
        self.createDoedsmelding = function() {

        };

        // Oppdater dødsmelding på ident
        self.updateDoedsmelding = function() {

        };

        // annuller en dødsmelding
        self.deleteDoedsmelding = function() {

        };

        // Lagre dødsmelding til db
        self.storeDoedsmelding = function() {

        };

        // Lese dødsmeldinger fra db
        self.retriveDoedsmelding = function(id) {
            return $http.get(url + '/liste/' + id);
        };
    }]);


