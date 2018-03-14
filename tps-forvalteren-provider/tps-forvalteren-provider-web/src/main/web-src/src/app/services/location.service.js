
angular.module('tps-forvalteren.service')
    .service('locationService', ['$state', '$location', function($state, $location) {

        var self = this;
        var returnState = 'root';

        self.redirectToHomeState = function() {
            $state.go(returnState);
        };

        self.redirectToLoginState = function() {
            $state.go('login');
        };

        self.redirectToEndringState = function () {
            $state.go('endringer');
        };

        self.redirectToServiceRutineState = function (rutine) {
            $state.go('servicerutine', {serviceRutineName: rutine});
        };

        self.redirectToGT = function() {
            $state.go("gt");
        };

        self.redirectToTestgruppe = function() {
            $state.go("testgruppe");
        };

        self.redirectToVisTestdata = function(index) {
            $state.go("vis-testdata", {gruppeId: index});
        };

        self.redirectToOpprettTestdata = function(index) {
            $state.go("opprett-testdata", {gruppeId: index});
        };

        self.redirectToSkdEndringsmeldingGrupper = function() {
            $state.go("skd-meldingsgrupper");
        };

        self.redirectToOpprettSkdMeldinger = function(index) {
            $state.go("skd-vis-meldingsgruppe", {gruppeId: index});
        };

        self.isServicerutineState = function(){
            return $state.current.name === 'servicerutine';
        };

        self.redirectToSendDoedsmeldinger = function(){
            $state.go("send-doedsmeldinger");
        }

        self.redirectToRawXmlMelding = function(){
            $state.go("xml-melding");
        }

        self.redirectUrl = function(url, param) {
            if (url.indexOf('/:') !== -1) {
                $state.go(url.substr(1).replace(/\/:\s*\S*/, ''), param);
            }
            else {
                switch (url) {
                    case '/endringsmelding/skd/grupper':
                        self.redirectToSkdEndringsmeldingGrupper();
                        break;
                    case '/testgruppe':
                        self.redirectToTestgruppe();
                        break;
                    default:
                        self.redirectToHomeState();
                }
            }
        };

        self.isRoot = function () {
            return $location.url() === '/';
        };
    }]);