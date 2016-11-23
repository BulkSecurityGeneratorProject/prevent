'use strict';

angular.module('preventApp')
    .directive('activeMenu', function ($translate, $locale, tmhDynamicLocale) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var language = attrs.activeMenu;

                scope.$watch(function () {
                    return $translate.use();
                }, function (selectedLanguage) {
                    if (language === selectedLanguage) {
                        tmhDynamicLocale.set(language);
                        element.addClass('active');
                    } else {
                        element.removeClass('active');
                    }
                });
            }
        };
    })
    .directive('activeLink', function (location) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var clazz = attrs.activeLink;
                var path = attrs.href;
                path = path.substring(1); //hack because path does bot return including hashbang
                scope.location = location;
                scope.$watch('location.path()', function (newPath) {
                    if (path === newPath) {
                        element.addClass(clazz);
                    } else {
                        element.removeClass(clazz);
                    }
                });
            }
        };
    })
    .directive('sidebarToggle', function () {
        return {
            restrict: 'A',
            compile: function (element, attr) {
                if (attr.toggle === 'offcanvas') {
                    element.click(function (e) {
                        e.preventDefault();
                        //(attr);
                        //If window is small enough, enable sidebar push menu
                        if (angular.element(window).width() <= 992) {
                            angular.element('#skin').toggleClass("sidebar-open");
                            angular.element('.row-offcanvas').toggleClass('active');
                            angular.element('.left-side').removeClass("collapse-left");
                            angular.element(".right-side").removeClass("strech");
                            angular.element('.row-offcanvas').toggleClass("relative");
                        } else {
                            //Else, enable content streching
                            //(angular.element('.left-side'));
                            angular.element('#skin').toggleClass("sidebar-collapse");
                            angular.element('.left-side').toggleClass("collapse-left");
                            angular.element(".right-side").toggleClass("strech");
                        }
                    });
                }
            }
        };
    })
    .directive('btn', function () {
        //Add hover support for touch devices
        return {
            restrict: 'C',
            link: function (scope, element, attrs) {
                element.bind('touchstart', function () {
                    $(this).addClass('hover');
                }).bind('touchend', function () {
                    $(this).removeClass('hover');
                });
            }
        };
    })
    .directive('treeview', function () {
        /*
         * SIDEBAR MENU
         * ------------
         * This is a custom plugin for the sidebar menu. It provides a tree view.
         *
         * Usage:
         * $(".sidebar).tree();
         *
         * Note: This plugin does not accept any options. Instead, it only requires a class
         *       added to the element that contains a sub-menu.
         *
         * When used with the sidebar, for example, it would look something like this:
         * <ul class='sidebar-menu'>
         *      <li class="treeview active">
         *          <a href="#>Menu</a>
         *          <ul class='treeview-menu'>
         *              <li class='active'><a href=#>Level 1</a></li>
         *          </ul>
         *      </li>
         * </ul>
         *
         * Add .active class to <li> elements if you want the menu to be open automatically
         * on page load. See above for an example.
         */
        $.fn.tree = function () {
            return this.each(function () {
                var btn = $(this).children("a").first();
                var menu = $(this).children(".treeview-menu").first();
                var isActive = $(this).hasClass('active');

                //initialize already active menus
                if (isActive) {
                    menu.show();
                    btn.children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
                }
                //Slide open or close the menu on link click
                btn.click(function (e) {
                    e.preventDefault();
                    if (isActive) {
                        //Slide up to close menu
                        menu.slideUp();
                        isActive = false;
                        btn.children(".fa-angle-down").first().removeClass("fa-angle-down").addClass("fa-angle-left");
                        btn.parent("li").removeClass("active");
                    } else {
                        //Slide down to open menu
                        menu.slideDown();
                        isActive = true;
                        btn.children(".fa-angle-left").first().removeClass("fa-angle-left").addClass("fa-angle-down");
                        btn.parent("li").addClass("active");
                    }
                });

                /* Add margins to submenu elements to give it a tree look */
                menu.find("li > a").each(function () {
                    var pad = parseInt($(this).css("margin-left")) + 10;

                    $(this).css({
                        "margin-left": pad + "px"
                    });
                });

            });

        };
        return {
            restrict: 'C',
            link: function (scope, element, attrs) {
                element.tree();
            }
        };
    });
