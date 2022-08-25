export default {
    state: {
        user_id:0,
        socket: null,
        opponent_id:null,
        status:'matching',
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent_id) {
            state.opponent_id = opponent_id;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        updateUserId(state, userId) {
            state.user_id = userId;
        }

    },
    actions: {
    },
    modules: {
    }
}