/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the MIT License (MIT) which accompanies this
 * distribution, and is available at https://opensource.org/licenses/MIT
 *
 * Contributors: Development Gateway - initial API and implementation
 ******************************************************************************/
// if nothing has changed that the user can leave the page without any
// confirmation
var shouldConfirmFormLeaving = true;
var formChanged = false;

function checkAnyChange() {
    $(':input').each(function () {
        $(this).on('change', function () {
            formChanged = true;
        });
    });

    // set formChanged when input has focus
    $(':input').each(function () {
        $(this).on('keyup', function () {
            formChanged = true;
        });
    });

    // for the next 2 rules check CCR-269
    $(document).on('drop', 'textarea', function (e) {
        // force focus on that element
        $(this).focus();

        var that = $(this);

        // when the drop happens the input value will not be updated yet
        // use a timeout to allow the input value to change.
        setTimeout(function () {
            that.trigger('change');
        }, 50);

        formChanged = true;
    });

    $(document).on('drop', ':input', function (e) {
        // force focus on that element
        $(this).focus();

        var that = $(this);

        // when the drop happens the input value will not be updated yet
        // use a timeout to allow the input value to change.
        setTimeout(function () {
            that.trigger('change');
        }, 50);

        formChanged = true;
    });
}

$(document).ready(function() { checkAnyChange(); });
/*
* Below works, but we only need to rebind on dynamic elements rendered based on some conditions.
* Therefore better to append BIND_FORM_LEAVING_CHECK when dynamic content is loaded.
$(document).ajaxComplete(function() { checkAnyChange(); });
*/

// confirmation modal before window unload
$(window).on('beforeunload', function (e) {
    if (shouldConfirmFormLeaving && formChanged) {
        e.preventDefault();
        // heads up: the latest browsers use their own generic message, not customizable here.
        return e.returnValue = "${formLeavingWarning}";
    }
});

function enableFormLeavingConfirmation() {
    shouldConfirmFormLeaving = true;
}

function disableFormLeavingConfirmation() {
    shouldConfirmFormLeaving = false;
}

function markFormAsChanged() {
    formChanged = true;
}
