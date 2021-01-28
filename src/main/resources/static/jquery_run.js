
const option = $("<option></option>");
const tr = $("<tr></tr>");
const td = $("<td></td>");
const select = $("<select disabled></select>").attr("name", "roleIds");
const buttonGlobal = $("<button type='button' data-toggle='modal'></button>");

let buttonEditUser;
let buttonDeleteUser;
let allUsers;
let tbody = $("#users");
let addLogin = $("#addLogin");
let addPassword = $("#addPassword");
let addEmail = $("#addEmail");
let addFirstName = $("#addFirstName");
let addLastName = $("#addLastName");
let addAge = $("#addAge");
let addRoles = $("#addRoles");

$(document).ready(function () {
    $.ajax("/api", {
        dataType: "json", success: function (msg) {
            allUsers = msg;
            msg.forEach(function (el) {
                addUserInTableBody(el);
            });

            bindButtonEditDelete();
        }
    })
})

function editUser() {

    let optionRoles = $("#roles")[0];
    let selectedRoles = [];

    for (let i = 0; i < optionRoles.options.length; i++) {
        if (optionRoles.options[i].selected) {
            selectedRoles.push({id: parseInt(optionRoles.options[i].value), role: optionRoles.options[i].text});
        }
    }

    $.ajax("/api/admin/edit", {
            method: "post",
            contentType: 'application/json',
            data:
                JSON.stringify(
                    {
                        id: parseInt($("#editModalUser").attr("value")),
                        login: $("#editLogin").val(),
                        password: $("#editPassword").val(),
                        email: $("#editEmail").val(),
                        firstName: $("#editFirstName").val(),
                        lastName: $("#editLastName").val(),
                        age: parseInt($("#editAge").val()),
                        role: selectedRoles
                    }),

            dataType: "json",

        success: function (msg) {
            $("#login" + msg.id).text(msg.login);
            $("#password" + msg.id).text(msg.password);
            $("#email" + msg.id).text(msg.email);
            $("#firstName" + msg.id).text(msg.firstName);
            $("#lastName" + msg.id).text(msg.lastName);
            $("#age" + msg.id).text(msg.age);
            $("#select" + msg.id).children().remove().attr("size", msg.role.length);

            msg.role.forEach(function (role) {
                option
                    .clone().text(role.role)
                    .appendTo($("#select" + msg.id));
            });

        }
    })

    editUserUnselectRoles();
    $("#modalEdit").modal('hide');
}

function editUserUnselectRoles() {
    $("[name=roleOption]").attr("selected", false);

}

function deleteUser() {
    $.ajax("/api/admin/delete", {
        method: "post",
        data: {id: $("#deleteModalUser").attr("value")},
        dataType: "text",

        success: function (msg) {
            $("#tr" + msg).children().remove();
        }

    });

    $("#modalDelete").modal('hide');
}

function addUser() {

    let optionRoles = $("#addRoles")[0];
    let selectedRoles = [];

    for (let i = 0; i < optionRoles.options.length; i++) {
        if (optionRoles.options[i].selected) {
            selectedRoles.push({id: parseInt(optionRoles.options[i].value), role: optionRoles.options[i].text});
        }
    }

    $.ajax("/api/admin/add", {
        method: "post",
        contentType: 'application/json',
        data:
             JSON.stringify(
                    {
                        login: addLogin.val(),
                        password: addPassword.val(),
                        email: addEmail.val(),
                        firstName: addFirstName.val(),
                        lastName: addLastName.val(),
                        age: addAge.val(),
                        role: selectedRoles
                    }),

        dataType: "json",
        success: function (msg) {
            addUserInTableBody(msg);
            allUsers.push(msg);
            bindButtonEditDelete();

            addLogin.val("");
            addPassword.val("");
            addEmail.val("");
            addFirstName.val("");
            addLastName.val("");
            addAge.val("");
            addRoles.val("");
        }
    })
}

function addUserInTableBody(el) {
    let trLocal = tr.clone();
    trLocal.attr("id", "tr" + el.id);

    getTd(el.login, "login" + el.id).appendTo(trLocal);
    getTd(el.password, "password" + el.id).appendTo(trLocal);
    getTd(el.email, "email" + el.id).appendTo(trLocal);
    getTd(el.firstName, "firstName" + el.id).appendTo(trLocal);
    getTd(el.lastName, "lastName" + el.id).appendTo(trLocal);
    getTd(el.age, "age" + el.id).appendTo(trLocal);
    getSelect(el).appendTo(trLocal);
    getButton("primary", "Edit", el.id).appendTo(trLocal);
    getButton("danger", "Delete", el.id).appendTo(trLocal);
    trLocal.appendTo(tbody);
}

let getSelect = function (el) {
    let selectLocal = select.clone();
    selectLocal.attr("size", el.role.length);
    selectLocal.attr("id", "select" + el.id);
    el.role.forEach(function (role) {
        option.clone().text(role.role).appendTo(selectLocal);
    });
    let tdSelect = td.clone();
    selectLocal.appendTo(tdSelect);
    return tdSelect;
};

let getTd = function (val, valueOfName) {
    let tdLocal = td.clone();
    tdLocal.attr("id", valueOfName)
    return tdLocal.text(val);
}

let getButton = function (class1, text1, userId) {
    let button = buttonGlobal.clone();
    button.attr("id", text1 + "User");
    button.attr("class", "btn btn-" + class1);
    button.attr("value", userId);
    button.attr("name", "user" + text1 + "CallModal");
    button.text(text1);
    let tdButton = td.clone();
    button.appendTo(tdButton);
    return tdButton;

}

function fillModalFields(action, user) {
    $("#" + action + "Login").attr("value", user.login)
    $("#" + action + "Password").attr("value", user.password)
    $("#" + action + "Email").attr("value", user.email)
    $("#" + action + "FirstName").attr("value", user.firstName)
    $("#" + action + "LastName").attr("value", user.lastName)
    $("#" + action + "Age").attr("value", user.age)
    $("#" + action + "ModalUser").attr("value", user.id)
    $.each(user.role, function (key, value) {
        $("#" + action + "Role" + value.id).attr("selected", true)
    })
}

function bindButtonEditDelete() {
    buttonEditUser = $("[name=userEditCallModal]");
    buttonEditUser.click(
        function () {
            fillModalFields("edit", getUser(parseInt($(this).attr("value"))));
            $("#modalEdit").modal('show');
        }
    )
    buttonDeleteUser = $("[name=userDeleteCallModal]");
    buttonDeleteUser.click(function () {
        fillModalFields("delete", getUser(parseInt($(this).attr("value"))));
        $("#modalDelete").modal('show');
    })
}

let getUser = function (id) {
    let user;
    $.each(allUsers, function (index, object) {
        $.each(object, function (key, value) {
            if (key === "id" && value === id) {
                user = object;
            }
        })
    })
    return user;
}
