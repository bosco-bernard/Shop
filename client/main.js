/* 
  * Fires an API call to the server and adds the reported city as an alien city
  */
 function postShopLocation() {
    var city = $("#city-post-input").val();

    // Fires an Ajax call to the URL defined in the index.js function file
// All URLs to the Advanced I/O function will be of the pattern: /server/{function_name}/{url_path}
    $.ajax({
        url: "/server/ShopAIO/shop", //If your Advanced I/O function is coded in the Java environment, replace the "alien_city_function" with "AlienCityAIO"
        type: "post",
        contentType: "application/json",
        data: JSON.stringify({
            "city_name": city
        }),
        success: function (data) {
            alert(data.message);
        },
        error: function (error) {
            alert(error.message);
        }
    });
}

/**
 * Fires an API call to the server to check whether the given city is alien city or not
 */
function getShopLocation() {
    showLoader();
    
    var city = $("#city-get-input").val();

  // Fires an Ajax call to the URL defined in the index.js function file
 // All URLs to the function will be of the pattern: /server/{function_name}/{url_path}
    $.ajax({
        url: "/server/ShopAIO/shop?city_name=" + city, //If your Advanced I/O function is coded in the Java environment, replace the "alien_city_function" with "AlienCityAIO"
        type: "get",
        success: function (data) {
            console.log(data);
            $("#result-text").text("");
            $("#result-text").text(data.message);
            hideLoader();
        },
        errror: function (error) {
            alert(error.message);
        }
    });
}

function showLoader()
{
    $("#result-container").hide();
    $("#loader").show();
}

function hideLoader()
{
    $("#loader").hide();
    $("#result-container").show();
}
