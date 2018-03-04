var paramName="_csrf";

function getRequest(url, data, success, fail)
{
    $.ajax({
        url:url,
        data:data,
        method:"GET",
        contentType:"application/json"
    }).done(success).fail(fail);
}
function postRequest(url, data, success, fail)
{
    data[paramName]=getSecurityToken();
    $.ajax({
        url:url,
        data:data,
        method:"POST",
        contentType:"application/json"
    }).done(success).fail(fail);
}

function getSecurityToken(){
    return $("input[name="+paramName+"]").val();
}