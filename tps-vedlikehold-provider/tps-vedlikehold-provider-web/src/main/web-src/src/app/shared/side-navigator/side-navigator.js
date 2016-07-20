/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', '$mdDialog', 'servicerutineFactory', function($scope, $mdDialog, servicerutineFactory, testy){//servicerutinePromise, environmentPromise) {
        var displayingErrorMessage = false;

        console.log(testy.value);

        $scope.getServicerutineInternNavn = function(serviceRutinenavn) {
            console.log('getServicerutineInternNavn ' + serviceRutinenavn);
            return servicerutineFactory.getServicerutineInternNavn(serviceRutinenavn);
        };
        
        function showAlertApiError(msg) {
            if (!displayingErrorMessage) {
                displayingErrorMessage = true;
                var confirm = $mdDialog.confirm()
                    .title('Serverfeil')
                    .textContent('Fikk ikke hentet informasjon om ' + msg + ' fra server. Vil du prøve igjen?')
                    .ariaLabel('Feil ved henting av ' + msg)
                    .ok('Prøv igjen')
                    .cancel('Avbryt');
                $mdDialog.show(confirm).then(function () {
                    // getFromServerServicerutiner();
                    // getFromServerEnvironments();
                }).finally(function(){
                    displayingErrorMessage = false;
                });
            }
        }

        // function getFromServerServicerutiner() {
        //     if (!servicerutineFactory.isSetServicerutiner()) {
        //         servicerutineFactory.getFromServerServicerutiner().then(function (res) {
        //             servicerutineFactory.setServicerutiner(res.data);
        //         }, function (error) {
        //             showAlertApiError('servicerutiner');
        //         });
        //     }
        //     $scope.servicerutiner = servicerutineFactory.getServiceRutinenavn();
        // }

        // function getFromServerEnvironments() {
        //     if (!servicerutineFactory.isSetEnvironments()) {
        //         servicerutineFactory.getFromServerEnvironments().then(function (res) {
        //             servicerutineFactory.setEnvironments(res.data);
        //         }, function (error) {
        //             showAlertApiError('miljøer');
        //         });
        //     }
        // }
        
        // function init() {
        //     getFromServerServicerutiner();
        //     getFromServerEnvironments();
        // }
        //
        // init();

        function init() {
            if (servicerutineFactory.isSetServicerutiner()) {
                $scope.servicerutiner = servicerutineFactory.getServicerutiner();
            } else {
                showAlertApiError('servicerutiner');
            }
        }

        init();
    }]);
