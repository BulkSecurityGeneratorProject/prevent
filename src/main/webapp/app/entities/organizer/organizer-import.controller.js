(function () {
    'use strict';

    angular
        .module('preventApp')
        .controller('OrganizerImportController', OrganizerImportController);

    OrganizerImportController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'Events', 'EventType', 'Locations', 'Upload'];

    function OrganizerImportController($timeout, $scope, $stateParams, $uibModalInstance, Events, EventType, Locations, Upload) {
        var vm = this;

        vm.clear = clear;

        vm.uploadFiles = function uploadFiles(file) {
            console.log(file)
            Upload.upload({
                url: 'api/import',
                data: {file: file, 'type': 'organizer'}
            }).then(function (resp) {
                clear();
                console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
            }, function (resp) {
                console.log('Error status: ' + resp.status);
            }, function (evt) {
                var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
            });
        };

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

    }
})();
