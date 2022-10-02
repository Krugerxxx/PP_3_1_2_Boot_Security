//Работа с данными текущего пользователя
let currentUser = {
    user: {},
    getUser: async () => {
        currentUser.user = await fetch('api/user')
            .then(response => response.json())
        updateHrefCurrentUser()
        updateUserPage()
    },

    /*update: async () => {
        currentUser.user = await fetch('/api/user')
            .then(response => response.json())
        updateHeader();
        updateCurrentUserInfo()

    },*/
    hasRole: (name) => {
        let result = false
        currentUser.user.roles.forEach(role => {
            if (role.name == name) {
                result = true
            }
        })
        return result
    },

    getRoles: () => {
        let roles = []
        currentUser.user.roles.forEach(role => roles.push(role.name))
        return roles
    }
}


//Обновление данных в заголовке текущего пользователя
async function updateHrefCurrentUser() {
    $('#currentUserName1').text(currentUser.user.username)
    $('#currentUserName2').text(currentUser.user.username)
    $('#currentUserRoles').text(currentUser.getRoles().join(' ').toString())
}


//Обновление данных на станице данных текущего пользователя
async function updateUserPage() {
    let value = $('#userPageTable tbody tr:first')
    value.find('td').eq(0).text(currentUser.user.id)
    value.find('td').eq(1).text(currentUser.user.firstname)
    value.find('td').eq(2).text(currentUser.user.lastname)
    value.find('td').eq(3).text(currentUser.user.age)
    value.find('td').eq(4).text(currentUser.user.email)
    value.find('td').eq(5).text(currentUser.getRoles().join(' ').toString())
}


//Работа со списком пользователей
let users = {
    userMap: new Map(),
    update: async () => {
        let userList = await fetch('/api/users').then(response => response.json())
        userList.forEach(user => users.userMap.set(user.id, user))
        updateUserTable()
    },/*
    remove: async (id) => {
        users.userMap.delete(id)
        updateUserTable()
    },*/
    save: async (user) => {
        users.userMap.set(user.id, user)
        updateUserTable()
    }
}


//Список всех ролей
let allRoles = {
    list: [],
    update: async () => {
        allRoles.list = await fetch('/api/users/roles')
            .then(response => response.json())
        //await updateRolesInNewUserForm()
    }
}


//Обновление списка пользователей
async function updateUserTable() {
    let table = $("#users-table tbody")
    table.html("")
    users.userMap.forEach(user => {
        let button_edit = '<button type="button" class="btn btn-info edit-button" data-toggle="modal" data-target="#editmodal" data-user-id="' + user.id + '">Edit</button> '
        let button_delete = '<button type="button" class="btn btn-danger delete-button" data-toggle="modal" data-target="#deleteModal" data-user-id="' + user.id + '">Delete</button>'
        let roles = []
        user.roles.forEach(role => roles.push(role.name))
        let value = "<tr>" +
            "<td>" + user.id + "</td>" +
            "<td>" + user.firstname + "</td>" +
            "<td>" + user.lastname + "</td>" +
            "<td>" + user.age + "</td>" +
            "<td>" + user.email + "</td>" +
            "<td>" + roles + "</td>" +
            "<td>" + button_edit + "</td>" +
            "<td>" + button_delete + "</td>" +
            "</tr>"
        table.append(value)
    })
}


/*
async function updateRolesInNewUserForm() {
    let select = $('#newUserForm select')
    select.html('')
    allRoles.list.forEach(role => select.append("<option value='"+role.name+"'>"+role.name))
}*/



$(document).ready(async function () {

    //Установление значения левой панели
    let page = location.pathname.substring(1)
    if (page == 'admin') {
        $('#home-tab').tab('show')
    } else {
        $('#profile-tab').tab('show')
    }
    $('#left-menu a').on('click', function (event) {
        event.preventDefault()
        window.history.pushState('', '', event.target.href);
        $(this).tab('show')
    })


    //Получение текущего пользователя и сопутствующая ему инициализация
    await currentUser.getUser()


    //Обновление списка пользователей
    if (currentUser.hasRole('ADMIN')) {
        await users.update()
        await allRoles.update()
    }


    //Json из данных формы
    function getJson(data) {
        let dataForm = new FormData(data);
        let user = {roles: []};
        dataForm.forEach((value, key) => {
            if (!Reflect.has(user, key)) {
                user[key] = value;
                return;
            }
            if (!Array.isArray(user[key])) {
                user[key] = [user[key]];
            }
            user[key].push(value);
        });
        user.enabled = (user.enabled == 'on')
        return JSON.stringify(user);
    }

    //Edit пользователя
    $('body').on('click', '.edit-button', function () {

        let userId = Number($(this).attr('data-user-id'))
        let user = users.userMap.get(userId)

        $('#editForm').trigger('reset')
        $('#editForm #edit_id').val(user.id)
        $('#editForm #edit_firstname').val(user.firstname)
        $('#editForm #edit_lastname').val(user.lastname)
        $('#editForm #edit_username').val(user.username)
        $('#editForm #edit_age').val(user.age)
        $('#editForm #edit_email').val(user.email)
        $('#editForm #edit_password').attr(user.password)
        let select = $('#editForm').find('#edit_roles')
        select.html('')
        allRoles.list.forEach(role => select.append("<option value='" + role.name + "'>" + role.name))
        user.roles.forEach(role => select.find('option[value=' + role.name + ']').prop('selected', true))
    })

    $('#editForm').on("submit", async function (event) {
        event.preventDefault();
        let userId = Number($(this).find('#edit_id').val())

        let json = getJson(event.currentTarget)
        let response = await fetch('/api/users/' + userId, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: json
        });

        if (response.status == 200) {
            await users.save(await response.json())
            $('#editmodal').modal('hide');
            this.reset()
        }
    })


});