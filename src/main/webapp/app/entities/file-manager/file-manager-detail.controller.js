(function() {
    'use strict';

    angular
        .module('preventApp')
        .controller('FileManagerDetailController', FileManagerDetailController);

    FileManagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FileManager', 'User'];

    function FileManagerDetailController($scope, $rootScope, $stateParams, previousState, entity, FileManager, User) {
        var vm = this;

        vm.fileManager = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('preventApp:fileManagerUpdate', function(event, result) {
            vm.fileManager = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
