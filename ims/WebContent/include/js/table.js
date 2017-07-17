/**
 * Use to set background-color of tr for mouseover and mouseout events
 * requirement: set tr class to data
 * Created by Eric Li on 2/27/2017.
 */
$(document).ready(function () {
    $("tr.data").mouseover(function () {
        $(this).css("background-color", "#ade97d");
    });
    $("tr.data").mouseout(function () {
        $(this).css("background-color", "#FFFFFF");
    });
});