angular.module('tps-forvalteren.testgruppe')
    .controller('TestgruppeCtrl', ['$scope', 'headerService', 'testdataService', 'utilsService',
        function ($scope, headerService, testdataService, utilsService) {

        headerService.setHeader('Testdata');

        $scope.testgrupper = [];

        $scope.aapneFane = function (index) {
            alert("Åpne fane med id = " + index);
        };

        testdataService.hentTestgrupper().then(
            function(result) {
                $scope.testgrupper = result;
            },
            function (error) {
                utilsService.showAlertError(error);
                $scope.testgrupper = [{id: "1", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "2", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "3", navn: "Gml pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "4", navn: "Triviell pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "5", navn: "Luguber pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "6", navn: "Trivelig pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "7", navn: "Ekstremt langt gr.navn pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "8", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "9", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "10", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "11", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "12", navn: "Gml pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "13", navn: "Triviell pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "14", navn: "Luguber pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "15", navn: "Trivelig pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "16", navn: "Ekstremt langt gr.navn pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "17", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"},
                    {id: "18", navn: "Ung pensjon", beskrivelse: "Testdata for pensjonssystemet"}
                ];
            }
        );
    }]);