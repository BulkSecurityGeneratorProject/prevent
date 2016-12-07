(function () {
    'use strict';

    angular
        .module('preventApp')
        .directive('moDateInput', moDateInput);

    moDateInput.$inject = ['DateUtils'];

    function moDateInput(DateUtils) {
        var directive = {
            restrict: 'A',
            require: '^ngModel',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs, parentCtrl) {
            // data.publishDate = DateUtils.convertLocalDateFromServer(data.publishDate);

            parentCtrl.$parsers.push(function (data) {
                //View -> Model
                return data;
            });
            parentCtrl.$formatters.push(function (data) {
                //Model -> View
                return new Date(data);
            });
        }
    }
})();
