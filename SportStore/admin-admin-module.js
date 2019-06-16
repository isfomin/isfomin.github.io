(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["admin-admin-module"],{

/***/ "./src/app/admin/admin.component.html":
/*!********************************************!*\
  !*** ./src/app/admin/admin.component.html ***!
  \********************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div class=\"navbar navbar-inverse bg-info\">\r\n  <a class=\"navbar-brand\">SPORTS STORE Admin</a>\r\n</div>\r\n<div class=\"m-t-1\">\r\n  <div class=\"col-xs-3\">\r\n    <button class=\"btn btn-outline-info btn-block\"\r\n            routerLink=\"/admin/main/products\"\r\n            routerLinkActive=\"active\">\r\n      Products\r\n    </button>\r\n    <button class=\"btn btn-outline-info btn-block\"\r\n            routerLink=\"/admin/main/orders\"\r\n            routerLinkActive=\"active\">\r\n      Orders\r\n    </button>\r\n    <button class=\"btn btn-outline-danger btn-block\" (click)=\"logout()\">\r\n      Logout\r\n    </button>\r\n  </div>\r\n  <div class=\"col-xs-9\">\r\n    <router-outlet></router-outlet>\r\n  </div>\r\n</div>\r\n"

/***/ }),

/***/ "./src/app/admin/admin.component.ts":
/*!******************************************!*\
  !*** ./src/app/admin/admin.component.ts ***!
  \******************************************/
/*! exports provided: AdminComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AdminComponent", function() { return AdminComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _model_auth_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../model/auth.service */ "./src/app/model/auth.service.ts");




var AdminComponent = /** @class */ (function () {
    function AdminComponent(auth, router) {
        this.auth = auth;
        this.router = router;
    }
    AdminComponent.prototype.logout = function () {
        this.auth.clear();
        this.router.navigateByUrl("/");
    };
    AdminComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            template: __webpack_require__(/*! ./admin.component.html */ "./src/app/admin/admin.component.html")
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_model_auth_service__WEBPACK_IMPORTED_MODULE_3__["AuthService"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["Router"]])
    ], AdminComponent);
    return AdminComponent;
}());



/***/ }),

/***/ "./src/app/admin/admin.module.ts":
/*!***************************************!*\
  !*** ./src/app/admin/admin.module.ts ***!
  \***************************************/
/*! exports provided: AdminModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AdminModule", function() { return AdminModule; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ "./node_modules/@angular/common/fesm5/common.js");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/forms */ "./node_modules/@angular/forms/fesm5/forms.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _auth_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./auth.component */ "./src/app/admin/auth.component.ts");
/* harmony import */ var _admin_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./admin.component */ "./src/app/admin/admin.component.ts");
/* harmony import */ var _auth_guard__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./auth.guard */ "./src/app/admin/auth.guard.ts");
/* harmony import */ var _productTable_component__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ./productTable.component */ "./src/app/admin/productTable.component.ts");
/* harmony import */ var _productEditor_component__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ./productEditor.component */ "./src/app/admin/productEditor.component.ts");
/* harmony import */ var _orderTable_component__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! ./orderTable.component */ "./src/app/admin/orderTable.component.ts");











var routing = _angular_router__WEBPACK_IMPORTED_MODULE_4__["RouterModule"].forChild([
    { path: "auth", component: _auth_component__WEBPACK_IMPORTED_MODULE_5__["AuthComponent"] },
    {
        path: "main", component: _admin_component__WEBPACK_IMPORTED_MODULE_6__["AdminComponent"], canActivate: [_auth_guard__WEBPACK_IMPORTED_MODULE_7__["AuthGuard"]],
        children: [
            { path: "products/:mode/:id", component: _productEditor_component__WEBPACK_IMPORTED_MODULE_9__["ProductEditorComponent"] },
            { path: "products/:mode", component: _productEditor_component__WEBPACK_IMPORTED_MODULE_9__["ProductEditorComponent"] },
            { path: "products", component: _productTable_component__WEBPACK_IMPORTED_MODULE_8__["ProductTableComponent"] },
            { path: "orders", component: _orderTable_component__WEBPACK_IMPORTED_MODULE_10__["OrderTableComponent"] },
            { path: "**", redirectTo: "products" }
        ]
    },
    { path: "**", redirectTo: "auth" }
]);
var AdminModule = /** @class */ (function () {
    function AdminModule() {
    }
    AdminModule = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["NgModule"])({
            imports: [_angular_common__WEBPACK_IMPORTED_MODULE_2__["CommonModule"], _angular_forms__WEBPACK_IMPORTED_MODULE_3__["FormsModule"], routing],
            providers: [_auth_guard__WEBPACK_IMPORTED_MODULE_7__["AuthGuard"]],
            declarations: [_auth_component__WEBPACK_IMPORTED_MODULE_5__["AuthComponent"], _admin_component__WEBPACK_IMPORTED_MODULE_6__["AdminComponent"], _productTable_component__WEBPACK_IMPORTED_MODULE_8__["ProductTableComponent"], _productEditor_component__WEBPACK_IMPORTED_MODULE_9__["ProductEditorComponent"], _orderTable_component__WEBPACK_IMPORTED_MODULE_10__["OrderTableComponent"]]
        })
    ], AdminModule);
    return AdminModule;
}());



/***/ }),

/***/ "./src/app/admin/auth.component.html":
/*!*******************************************!*\
  !*** ./src/app/admin/auth.component.html ***!
  \*******************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div class=\"bg-info p-a-1 text-xs-center\">\r\n  <h3>SportsStore Admin</h3>\r\n</div>\r\n<div class=\"bg-danger m-t-1 p-a-1 text-xs-center\" *ngIf=\"errorMessage!=null\">\r\n  {{errorMessage}}\r\n</div>\r\n<div class=\"p-a-1\">\r\n  <form novalidate #form=\"ngForm\" (ngSubmit)=\"authenticate(form)\">\r\n    <div class=\"form-group\">\r\n      <label>Name</label>\r\n      <input class=\"form-control\" name=\"username\" [(ngModel)]=\"username\" required/>\r\n      <div class=\"form-group\">\r\n        <label>Password</label>\r\n        <input class=\"form-control\" type=\"password\" name=\"password\" [(ngModel)]=\"password\" required/>\r\n      </div>\r\n      <div class=\"text-xs-center\">\r\n        <button class=\"btn btn-secondary\" routerLink=\"/\">Go back</button>\r\n        &nbsp;\r\n        <button class=\"btn btn-primary\" type=\"submit\">Log In</button>\r\n      </div>\r\n    </div>\r\n  </form>\r\n</div>\r\n"

/***/ }),

/***/ "./src/app/admin/auth.component.ts":
/*!*****************************************!*\
  !*** ./src/app/admin/auth.component.ts ***!
  \*****************************************/
/*! exports provided: AuthComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AuthComponent", function() { return AuthComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _model_auth_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../model/auth.service */ "./src/app/model/auth.service.ts");




var AuthComponent = /** @class */ (function () {
    function AuthComponent(router, auth) {
        this.router = router;
        this.auth = auth;
    }
    AuthComponent.prototype.authenticate = function (form) {
        var _this = this;
        if (form.valid) {
            this.auth.authenticate(this.username, this.password).subscribe(function (response) {
                if (response) {
                    _this.router.navigateByUrl("/admin/main");
                }
                _this.errorMessage = "Authentication Failed";
            });
        }
        else {
            this.errorMessage = "Form Date Invalid";
        }
    };
    AuthComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            template: __webpack_require__(/*! ./auth.component.html */ "./src/app/admin/auth.component.html")
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_angular_router__WEBPACK_IMPORTED_MODULE_2__["Router"], _model_auth_service__WEBPACK_IMPORTED_MODULE_3__["AuthService"]])
    ], AuthComponent);
    return AuthComponent;
}());



/***/ }),

/***/ "./src/app/admin/auth.guard.ts":
/*!*************************************!*\
  !*** ./src/app/admin/auth.guard.ts ***!
  \*************************************/
/*! exports provided: AuthGuard */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AuthGuard", function() { return AuthGuard; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _model_auth_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../model/auth.service */ "./src/app/model/auth.service.ts");




var AuthGuard = /** @class */ (function () {
    function AuthGuard(router, auth) {
        this.router = router;
        this.auth = auth;
    }
    AuthGuard.prototype.canActivate = function (route, state) {
        if (!this.auth.authenticated) {
            this.router.navigateByUrl("/admin/auth");
            return false;
        }
        return true;
    };
    AuthGuard = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Injectable"])(),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_angular_router__WEBPACK_IMPORTED_MODULE_2__["Router"], _model_auth_service__WEBPACK_IMPORTED_MODULE_3__["AuthService"]])
    ], AuthGuard);
    return AuthGuard;
}());



/***/ }),

/***/ "./src/app/admin/orderTable.component.html":
/*!*************************************************!*\
  !*** ./src/app/admin/orderTable.component.html ***!
  \*************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div class=\"form-check\">\r\n  <label class=\"form-check-label\">\r\n    <input type=\"checkbox\" class=\"form-check-input\" [(ngModel)]=\"includeShipped\"/>\r\n    Display Shipped Orders\r\n  </label>\r\n</div>\r\n<table class=\"table table-sm\">\r\n  <thead>\r\n    <tr><th>Name</th><th>Zip</th><th colspan=\"2\">Cart</th><th></th></tr>\r\n  </thead>\r\n  <tbody>\r\n    <tr *ngIf=\"getOrders().length == 0\">\r\n      <td colspan=\"5\">There are no orders</td>\r\n    </tr>\r\n    <ng-template ngFor let-o [ngForOf]=\"getOrders()\">\r\n      <tr>\r\n        <td>{{o.name}}</td><td>{{o.zip}}</td>\r\n        <th>Product</th><th>Quantity</th>\r\n        <td>\r\n          <button class=\"btn btn-warning\" (click)=\"markShipped(o)\">\r\n            Ship\r\n          </button>\r\n          <button class=\"btn btn-danger\" (click)=\"delete(o.id)\">\r\n            Delete\r\n          </button>\r\n        </td>\r\n      </tr>\r\n      <tr *ngFor=\"let line of o.cart.lines\">\r\n        <td colspan=\"2\"></td>\r\n        <td>{{line.product.name}}</td>\r\n        <td>{{line.quantity}}</td>\r\n      </tr>\r\n    </ng-template>\r\n  </tbody>\r\n</table>\r\n"

/***/ }),

/***/ "./src/app/admin/orderTable.component.ts":
/*!***********************************************!*\
  !*** ./src/app/admin/orderTable.component.ts ***!
  \***********************************************/
/*! exports provided: OrderTableComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "OrderTableComponent", function() { return OrderTableComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _model_order_repository__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../model/order.repository */ "./src/app/model/order.repository.ts");



var OrderTableComponent = /** @class */ (function () {
    function OrderTableComponent(repository) {
        this.repository = repository;
        this.includeShipped = false;
    }
    OrderTableComponent.prototype.getOrders = function () {
        var _this = this;
        return this.repository.getOrders().filter(function (o) { return _this.includeShipped || !o.shipped; });
    };
    OrderTableComponent.prototype.markShipped = function (order) {
        order.shipped = true;
        this.repository.updateOrder(order);
    };
    OrderTableComponent.prototype.delete = function (id) {
        this.repository.deleteOrder(id);
    };
    OrderTableComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            template: __webpack_require__(/*! ./orderTable.component.html */ "./src/app/admin/orderTable.component.html")
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_model_order_repository__WEBPACK_IMPORTED_MODULE_2__["OrderRepository"]])
    ], OrderTableComponent);
    return OrderTableComponent;
}());



/***/ }),

/***/ "./src/app/admin/productEditor.component.html":
/*!****************************************************!*\
  !*** ./src/app/admin/productEditor.component.html ***!
  \****************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<div class=\"bg-primary p-a-1\" [class.bg-warning]=\"editing\">\r\n  <h5 class=\"m-a-0\">{{editing ? \"Edit\" : \"Create\"}} Product</h5>\r\n</div>\r\n<form novalidate #form=\"ngForm\" (ngSubmit)=\"save(form)\">\r\n  <div class=\"form-group\">\r\n    <label>Name</label>\r\n    <input class=\"form-control\" name=\"name\" [(ngModel)]=\"product.name\"/>\r\n  </div>\r\n  <div class=\"form-group\">\r\n    <label>Category</label>\r\n    <input class=\"form-control\" name=\"category\" [(ngModel)]=\"product.category\"/>\r\n  </div>\r\n  <div class=\"form-group\">\r\n    <label>Description</label>\r\n    <textarea class=\"form-control\" name=\"description\" [(ngModel)]=\"product.description\"></textarea>\r\n  </div>\r\n  <div class=\"form-group\">\r\n    <label>Price</label>\r\n    <input class=\"form-control\" name=\"price\" [(ngModel)]=\"product.price\"/>\r\n  </div>\r\n  <button type=\"submit\" class=\"btn btn-primary\" [class.btn-warning]=\"editing\">\r\n    {{editing ? \"Save\" : \"Create\"}}\r\n  </button>\r\n  &nbsp;\r\n  <button type=\"reset\" class=\"btn btn-secondary\" routerLink=\"/admin/main/products\">\r\n    Cancel\r\n  </button>\r\n</form>\r\n"

/***/ }),

/***/ "./src/app/admin/productEditor.component.ts":
/*!**************************************************!*\
  !*** ./src/app/admin/productEditor.component.ts ***!
  \**************************************************/
/*! exports provided: ProductEditorComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ProductEditorComponent", function() { return ProductEditorComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "./node_modules/@angular/router/fesm5/router.js");
/* harmony import */ var _model_product_model__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../model/product.model */ "./src/app/model/product.model.ts");
/* harmony import */ var _model_product_repository__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ../model/product.repository */ "./src/app/model/product.repository.ts");





var ProductEditorComponent = /** @class */ (function () {
    function ProductEditorComponent(repository, router, activeRoute) {
        this.repository = repository;
        this.router = router;
        this.editing = false;
        this.product = new _model_product_model__WEBPACK_IMPORTED_MODULE_3__["Product"]();
        this.editing = activeRoute.snapshot.params["mode"] == "edit";
        if (this.editing) {
            Object.assign(this.product, repository.getProduct(activeRoute.snapshot.params["id"]));
        }
    }
    ProductEditorComponent.prototype.save = function (form) {
        this.repository.saveProduct(this.product);
        this.router.navigateByUrl("/admin/main/products");
    };
    ProductEditorComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            template: __webpack_require__(/*! ./productEditor.component.html */ "./src/app/admin/productEditor.component.html")
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_model_product_repository__WEBPACK_IMPORTED_MODULE_4__["ProductRepository"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["Router"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["ActivatedRoute"]])
    ], ProductEditorComponent);
    return ProductEditorComponent;
}());



/***/ }),

/***/ "./src/app/admin/productTable.component.html":
/*!***************************************************!*\
  !*** ./src/app/admin/productTable.component.html ***!
  \***************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = "<table class=\"table table-sm table-striped\">\r\n  <thead>\r\n    <tr>\r\n      <th>ID</th><th>Name</th><th>Category</th><th>Price</th>\r\n      <th></th>\r\n    </tr>\r\n  </thead>\r\n  <tbody>\r\n    <tr *ngFor=\"let p of getProducts()\">\r\n      <td>{{p.id}}</td>\r\n      <td>{{p.name}}</td>\r\n      <td>{{p.category}}</td>\r\n      <td>{{p.price | currency:\"USD\":true:\"2.2-2\"}}</td>\r\n      <td>\r\n        <button class=\"btn btn-sm btn-warning\" [routerLink]=\"['/admin/main/products/edit', p.id]\">\r\n          Edit\r\n        </button>\r\n        &nbsp;\r\n        <button class=\"btn btn-sm btn-danger\" (click)=\"deleteProduct(p.id)\">\r\n          Delete\r\n        </button>\r\n      </td>\r\n    </tr>\r\n  </tbody>\r\n</table>\r\n<button class=\"btn btn-primary\" routerLink=\"/admin/main/products/create\">\r\n  Create New Product\r\n</button>\r\n"

/***/ }),

/***/ "./src/app/admin/productTable.component.ts":
/*!*************************************************!*\
  !*** ./src/app/admin/productTable.component.ts ***!
  \*************************************************/
/*! exports provided: ProductTableComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "ProductTableComponent", function() { return ProductTableComponent; });
/* harmony import */ var tslib__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! tslib */ "./node_modules/tslib/tslib.es6.js");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "./node_modules/@angular/core/fesm5/core.js");
/* harmony import */ var _model_product_repository__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../model/product.repository */ "./src/app/model/product.repository.ts");



var ProductTableComponent = /** @class */ (function () {
    function ProductTableComponent(repository) {
        this.repository = repository;
    }
    ProductTableComponent.prototype.getProducts = function () {
        return this.repository.getProducts();
    };
    ProductTableComponent.prototype.deleteProduct = function (id) {
        this.repository.deleteProduct(id);
    };
    ProductTableComponent = tslib__WEBPACK_IMPORTED_MODULE_0__["__decorate"]([
        Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["Component"])({
            template: __webpack_require__(/*! ./productTable.component.html */ "./src/app/admin/productTable.component.html")
        }),
        tslib__WEBPACK_IMPORTED_MODULE_0__["__metadata"]("design:paramtypes", [_model_product_repository__WEBPACK_IMPORTED_MODULE_2__["ProductRepository"]])
    ], ProductTableComponent);
    return ProductTableComponent;
}());



/***/ })

}]);
//# sourceMappingURL=admin-admin-module.js.map