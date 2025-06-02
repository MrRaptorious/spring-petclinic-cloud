'use strict';

angular.module('invoice')
    .controller('InvoiceController', ['$http', '$state', '$stateParams', '$filter', function ($http, $state, $stateParams, $filter) {
        var self = this;


        var getUrl="api/invoice/invoice?visitId=" + ($stateParams.visitId || 0)
        $http.get(getUrl).then(function (resp) {
            if(resp.data.invoiceData != null){
                self.invoice = {
                    ...resp.data.invoiceData,
                    dueDate: new Date(resp.data.invoiceData.dueDate)
                }
                console.log("Served by Pod:", resp.data.servedByPod)
            }

        });

        var url = "api/invoice/visits/" + ($stateParams.visitId || 0) + "/invoice";
        self.submit = function () {
            var data = {
                ...self.invoice,
                dueDate: $filter('date')(self.invoice.dueDate, 'yyyy-MM-dd')
            };

            $http.post(url, data).then(function () {
                $state.go('owners');
            });
        };
    }]);
    