$(async function () {
    await getNavbarInfo();
    await getBothFunctionsModal();

})

const fetchingService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },

    getAllUsers: async () =>
        await fetch('admin/users'),

    getAllRoles: async () =>
        await fetch('admin/roles'),

    getLoggedUser: async () =>
        await fetch('/admin/loggedUser'),

    getSelectedUser: async (id) =>
        await fetch(`admin/users/${id}`),

    saveUserToDB: async (user) =>
        await fetch('admin/users', {
        method: 'POST',
        headers: fetchingService.head,
        body: JSON.stringify(user)}),

    updateUserDB: async (user, id) =>
        await fetch(`admin/users/${id}`, {
        method: 'PATCH',
        headers: fetchingService.head,
        body: JSON.stringify(user)}),

    deleteUserDB: async (id) =>
        await fetch(`admin/users/${id}`, {
        method: 'DELETE',
        headers: fetchingService.head})
}

async function getNavbarInfo() {

    await fetchingService.getLoggedUser()
        .then(res => {
            return res.json()
        })
        .then(data => {
            console.log(data.id)
            console.log(data.firstName)
            console.log(data.email)
            let s = ""
            if (data.roles != null) {
                data.roles.forEach(role => {
                        s += role.name + " "
                    })
                }

            $('#iddqd')
                .append(`${data.email} with roles: ${s}`)

            if(s.includes("ROLE_ADMIN")){
                $(async function() {
                    await getAllRolesListForAll();
                    getAllUserTable();
                    getAddNewUserForm();
                })

                $('#v-pills-home-tab').tab('show')
            } else {
                $('#v-pills-profile-tab').tab('show')
            }

            getLoggedUserTable(data)
        })
}


async function getAllRolesListForAll() {
    await fetchingService.getAllRoles()
        .then(res => res.json())
        .then(roleList => {
            window.allRoles = roleList;
        console.log("RolesForAllFunc - "  + allRoles)
        })
}



//юзер пилл
async function getLoggedUserTable(user) {
    let tableLogged = $('#loggedUserTable tbody');
    tableLogged.empty();

                let s = ""
                if (user.roles != null) {
                    user.roles.forEach(role => {
                        s += role.name + " "

                    })
                }
                console.log(s)
                    let tableLoggedContent = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${s}</td>  

                        </tr>
                )`;
                    tableLogged.append(tableLoggedContent);
}


async function getAllUserTable() {
    let tableAllUsers = $('#allUsersTable tbody');
    tableAllUsers.empty();
    console.log("Window add1 " + allRoles)
    await fetchingService.getAllUsers()
        .then(res => res.json())
        .then(userList => {
            userList.forEach(user => {
                let s = ""
                if (user.roles != null){user.roles.forEach(role => {
                    s += role.name + " "})}
                console.log(s)
                let tableAllUsersContent = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${s}</td>  
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-primary eBtn" 
                                data-toggle="modal" data-target="#bothModalsInOne">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger delBtn" 
                                data-toggle="modal" data-target="#bothModalsInOne">Delete</button>
                            </td>
                        </tr>
                )`;
                tableAllUsers.append(tableAllUsersContent);
            })
        })



    $("#allUsersTable").find('button').on('click', (event) => {
        let bothModalsInOne = $('#bothModalsInOne');
        let pointBut = $(event.target);
        let pointUsId = pointBut.attr('data-userid');
        let pointAction = pointBut.attr('data-action');
        bothModalsInOne.attr('data-userid', pointUsId);
        bothModalsInOne.attr('data-action', pointAction);
        bothModalsInOne.modal('show');
    })
}

async function getAddNewUserForm() {
    let form = $(`#newUserForm`)
    form.show();
        let targetForm = $('#newUserRoleBox');
        let optionLine = `<select class="form-control" name="userRoles" id="roleSelectBox" multiple size="${allRoles.length}">`;
        allRoles.forEach(role => {
            optionLine = optionLine + `<option value="${role.name}">${role.name}</option>`;
            console.log("role:" + role)
            console.log("role id:" + role.id)
            console.log("role name:" + role.name)
        })
        optionLine = optionLine + `</select>`
        targetForm.append(optionLine);


    $('#addUserToDBButt').click(async () =>  {
        let newUserDBForm = $('#newUserForm')
        let firstName = newUserDBForm.find('#add-username').val();
        let lastName = newUserDBForm.find('#add-surname').val();
        let age = newUserDBForm.find('#add-age').val();
        let email = newUserDBForm.find('#add-email').val();
        let password = newUserDBForm.find('#add-password').val();
        let roles = newUserDBForm.find('#roleSelectBox').val();

        let data = {
            firstName: firstName,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: roles
        }
        const response = await fetchingService.saveUserToDB(data);
        if (response.ok) {
            getAllUserTable();
            newUserDBForm.find('#add-username').val('');
            newUserDBForm.find('#add-surname').val('');
            newUserDBForm.find('#add-age').val('');
            newUserDBForm.find('#add-email').val('');
            newUserDBForm.find('#add-password').val('');
            newUserDBForm.find('#roleSelectBox').val('');
            $('#nav-home-tab').tab('show')
        }

    })
}


// модальное окно - откр
async function getBothFunctionsModal() {
    console.log('bothfunc')
    $('#bothModalsInOne').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let selModal = $(event.target);
        let userid = selModal.attr('data-userid');
        let action = selModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(selModal, userid);
                break;
            case 'delete':
                deleteUser(selModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let selModal = $(e.target);
        selModal.find('.modal-title').html('');
        selModal.find('.modal-body').html('');
        selModal.find('.modal-footer').html('');
    })
}

// Эдит форм инсерт
async function editUser(modal, id) {
    console.log("Window edit " + allRoles)
    let resp = await fetchingService.getSelectedUser(id);
    let user = resp.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button  class="btn btn-primary" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let editUserFormContent = `
            <form class="form-group" id="editUser">
                
                        <div class="form-group">
                            <label for="idEdit" class="col-form-label">User Id:</label>
                            <input type="text" readonly="readonly" class="form-control" id="idEdit" name="id" value="${user.id}">
                        </div>
                        <div class="form-group">
                            <label for="usernameEdit" class="col-form-label">First Name</label>
                            <input type="text" class="form-control" id="usernameEdit" required name="firstName" value="${user.firstName}">
                        </div>
                        <div class="form-group">
                            <label for="surnameEdit" class="col-form-label">Last Name</label>
                            <input type="text" class="form-control" id="surnameEdit" required name="lastName" value="${user.lastName}">
                        </div>
                        <div class="form-group">
                            <label for="ageEdit" class="col-form-label">Age</label>
                            <input type="text" class="form-control" id="ageEdit" required name="age" value="${user.age}">
                        </div>
                        <div class="form-group">
                            <label for="emailEdit" class="col-form-label">Email</label>
                            <input type="text" class="form-control" id="emailEdit" required name="email" value="${user.email}">
                        </div>
                        <div class="form-group">
                            <label for="passwordEdit" class="col-form-label">Password</label>
                            <input type="text" class="form-control" id="passwordEdit" name="password" value="">
                        </div>
                        
                        <div class="form-group">
                            <label for="roleBoxEdit" class="col-form-label">Role </label>
                            <div id="roleBoxEdit"></div>
                        </div>
            </form>
        `;

        modal.find('.modal-body').append(editUserFormContent);
            let targetForm = $('#roleBoxEdit');
            let optionLine = `<select class="form-control" name="userRoles" id="roleSelectBoxEdit" multiple size="${allRoles}">`;
            allRoles.forEach(role => {
                optionLine = optionLine + `<option value="${role.name}">${role.name}</option>`;
            })
            optionLine = optionLine + `</select>`
            targetForm.append(optionLine);
    })

    $("#editButton").on('click', async () => {
        let id = modal.find("#idEdit").val();
        let firstName = modal.find("#usernameEdit").val();
        let lastName = modal.find("#surnameEdit").val();
        let age = modal.find("#ageEdit").val();
        let email = modal.find("#emailEdit").val();
        let password = modal.find("#passwordEdit").val();
        let roles = modal.find("#roleSelectBoxEdit").val()

        let data = {
            id: id,
            firstName: firstName,
            lastName: lastName,
            age: age,
            email: email,
            password: password,
            roles: roles

        }
        const response = await fetchingService.updateUserDB(data, id);

        if (response.ok) {
            getAllUserTable();
            modal.modal('hide');

        }
    })
}

// делит форм инсерт
async function deleteUser(modal, id) {
    console.log('delete modal')
    let resp = await fetchingService.getSelectedUser(id);
    let user = resp.json();

    modal.find('.modal-title').html('Delete user');

    let deleteButton = `<button  class="btn btn-danger" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let deleteUserFormContent = `
            <form class="form-group" id="editUser">
                        <div class="form-group">
                            <label for="idEdit" class="col-form-label">ID</label>
                            <input type="text" readonly="readonly" class="form-control" id="idEdit" name="id" value="${user.id}">
                        </div>
                        <div class="form-group">
                            <label for="usernameEdit" class="col-form-label">First Name</label>
                            <input type="text" readonly="readonly" class="form-control" id="usernameEdit" required name="firstName" value="${user.firstName}">
                        </div>
                        <div class="form-group">
                            <label for="surnameEdit" class="col-form-label">Last Name</label>
                            <input type="text" readonly="readonly" class="form-control" id="surnameEdit" required name="lastName" value="${user.lastName}">
                        </div>
                        <div class="form-group">
                            <label for="ageEdit" class="col-form-label">Age</label>
                            <input type="text" readonly="readonly" class="form-control" id="ageEdit" required name="age" value="${user.age}">
                        </div>
                        <div class="form-group">
                            <label for="emailEdit" class="col-form-label">Email</label>
                            <input type="text" readonly="readonly" class="form-control" id="emailEdit" required name="email" value="${user.email}">
                        </div>
                        <div class="form-group" >
                            <label for="roleBoxDelete" class="col-form-label">Roles</label>
                            <div id="roleBoxDelete" readonly="readonly" ></div>
                        </div>
            </form>
        `;
        modal.find('.modal-body').append(deleteUserFormContent);
            let targetForm = $('#roleBoxDelete');
            let optionLine = `<select class="form-control"  id="roleSelectBoxDelete" readonly="readonly" name="userRoles" multiple size="${allRoles.length}">`;
            allRoles.forEach(role => {
                optionLine = optionLine + `<option value="${role.name}">${role.name}</option>`;
            })
            optionLine = optionLine + `</select>`
            targetForm.append(optionLine);
    })

    $("#deleteButton").on('click', async () => {
        const response = await fetchingService.deleteUserDB(id);
        if (response.ok) {
            getAllUserTable();
            modal.modal('hide');
        }
    })
}
