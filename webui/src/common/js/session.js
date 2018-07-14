let sessionSetObj = (name, obj) => {
    return sessionStorage.setItem(name, JSON.stringify(obj))
};

let sessionSetString = (name, str) => {
    return sessionStorage.setItem(name, str)
};

let sessionGetString = (name) => {
    return sessionStorage.getItem(name)
};

let sessionGetObj = (name) => {
    return JSON.parse(sessionStorage.getItem(name));
};

let sessionRemove = (name) => {
    sessionStorage.removeItem(name);
};

export default {
    storeToken: (token) => {
        sessionSetString('token', token);
    },

    fetchToken: () => {
        return sessionGetString('token');
    },

    storeUser: (user) => {
        sessionSetObj('user', user);
    },

    fetchUser: () => {
        return sessionGetObj('user');
    },

    getUserRole: () => {
        let user = sessionGetObj('user');
        if (user !== undefined) {
            let role = user.role;
            if (role !== undefined && ROLE[role] !== undefined) {
                return role;
            }
        }

        return 'ROLE_NORMAL';
    },

    cleanAll: () => {
        sessionRemove('user');
        sessionRemove('token');
    }
}