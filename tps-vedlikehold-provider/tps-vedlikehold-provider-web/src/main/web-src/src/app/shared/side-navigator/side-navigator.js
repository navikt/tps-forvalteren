/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', '$mdDialog', 'servicerutineFactory', function($scope, $mdDialog, servicerutineFactory) {
        var displayingErrorMessage = false;
        
        function showAlertApiError(msg) {
            console.log('showAlertApiError');
            if (!displayingErrorMessage) {
                displayingErrorMessage = true;
                var confirm = $mdDialog.confirm()
                    .title('Serverfeil')
                    .textContent('Fikk ikke hentet informasjon om ' + msg + ' fra server. Vil du prøve igjen?')
                    .ariaLabel('Feil ved henting av ' + msg)
                    .ok('Prøv igjen')
                    .cancel('Avbryt');
                $mdDialog.show(confirm).then(function () {
                    getFromServerServicerutiner();
                    getFromServerEnvironments()
                }).finally(function(){
                    displayingErrorMessage = false;
                });
            }
        }

        function getFromServerServicerutiner() {
            if (!servicerutineFactory.isSetServicerutiner()) {
                servicerutineFactory.getFromServerServicerutiner().then(function (res) {
                    servicerutineFactory.setServicerutiner(res.data);
                }, function (error) {
                    showAlertApiError('servicerutiner');
                });
            }
            $scope.servicerutiner = servicerutineFactory.getServicerutineNames();
        }

        function getFromServerEnvironments() {
            if (!servicerutineFactory.isSetEnvironments()) {
                servicerutineFactory.getFromServerEnvironments().then(function (res) {
                    servicerutineFactory.setEnvironments(res.data);
                }, function (error) {
                    showAlertApiError('miljøer');
                });
            }
        }
        
        function init() {
            getFromServerServicerutiner();
            getFromServerEnvironments();
        }
        
        init();
    }]);
