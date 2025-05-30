"use strict";

angular.module("invoice", ["ui.router"]).config([
  "$stateProvider",
  function ($stateProvider) {
    $stateProvider.state("invoice", {
      parent: "app",
      url: "/visits/:visitId/invoice",
      template: "<invoice></invoice>",
    });
  },
]);
