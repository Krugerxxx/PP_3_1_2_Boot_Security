//Получение текущего пользователя и сопутствующая ему инициализация
let currentUser = {
    user: {},
    getUser: async () => {
        currentUser.user = await fetch('api/user')
            .then(response => response.json())
        updateHrefCurrentUser()
        //updateUserPage()
    },

    /*update: async () => {
        currentUser.user = await fetch('/api/user')
            .then(response => response.json())
        updateHeader();
        updateCurrentUserInfo()

    },
    hasRole: (name) => {
        let result = false
        currentUser.user.roles.forEach(role => {
            if (role.name === name) {
                result = true
            }
        })
        return result
    },*/

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
/*async function updateUserPage() {
    let row = $('#userPageTable tbody tr:first')
    row.find('td').eq(0).text(currentUser.user.id)
    row.find('td').eq(1).text(currentUser.user.name)
    row.find('td').eq(2).text(currentUser.user.username)
    row.find('td').eq(3).text(currentUser.getRoleLabelList().toString())
    row.find('td').eq(4).text((currentUser.user.enabled) ? 'Yes' : 'No')
}*/



$(document).ready(async function () {

    //Получение текущего пользователя и сопутствующая ему инициализация
    await currentUser.getUser()


});