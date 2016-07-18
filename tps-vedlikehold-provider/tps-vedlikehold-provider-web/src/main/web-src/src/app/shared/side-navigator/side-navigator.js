/**
 * @author Frederik de Lichtenberg (Visma Consulting AS).
 * */
angular.module('tps-vedlikehold')
    .controller('navigatorCtrl', ['$scope', '$mdDialog', 'servicerutineFactory', function($scope, $mdDialog, servicerutineFactory) {
        // function showAlertServicerutine() {
        //     var confirm = $mdDialog.confirm()
        //         .title('Serverfeil')
        //         .textContent('Fikk ikke hentet informasjon om servicerutiner fra server. Vil du prøve igjen?')
        //         .ariaLabel('Feil ved henting av servicerutiner')
        //         .ok('Prøv igjen')
        //         .cancel('Avbryt');
        //     $mdDialog.show(confirm).then(function () {
        //         // getFromServerRequestInformation();
        //         getFromServerServicerutiner();
        //         getFromServerEnvironments();
        //     }, function () {
        //     });
        // }

        var displayingErrorMessage = false
        
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

        // function showAlertEnvironments() {
        //     var confirm = $mdDialog.confirm()
        //         .title('Serverfeil')
        //         .textContent('Fikk ikke hentet informasjon om miljøer fra server. Vil du prøve igjen?')
        //         .ariaLabel('Feil ved henting av miljøer')
        //         .ok('Prøv igjen')
        //         .cancel('Avbryt');
        //     $mdDialog.show(confirm).then(function () {
        //         getFromServerEnvironments();
        //     }, function () {
        //     });
        // }

        // function getFromServerRequestInformation() {
        //     // console.log(servicerutineFactory.getFromServerServicerutiner());
        //     // console.log(servicerutineFactory.getFromServerServicerutiner());
        //     if(servicerutineFactory.getFromServerServicerutiner() && servicerutineFactory.getFromServerEnvironments()) { //HVOR BURDE DETTE KALLET GJØRES?
        //         console.log('getFromServerRequestInformation - success');
        //         $scope.servicerutiner = servicerutineFactory.getServicerutineNames();
        //         console.log($scope.servicerutiner);
        //     }
        //     else {
        //         console.log('getFromServerRequestInformation - failure');
        //         servicerutineFactory.resetServerErrorFlags();
        //         showAlertApiError();
        //     }
        // }

        function getFromServerServicerutiner() {
            if (!servicerutineFactory.isSetServicerutiner()) {
                servicerutineFactory.getFromServerServicerutiner().then(function (res) {
                    servicerutineFactory.setServicerutiner(res.data);
                }, function (error) {
                    showAlertApiError('servicerutiner');
                });
            }
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
