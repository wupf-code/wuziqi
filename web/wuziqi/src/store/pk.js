export default {
    state: {
        user_id:0,
        socket: null,
        opponent_id:null,
        status:'matching',
        own_color:null,
        opponent_color:null,
        own_x:null,
        own_y:null,
        opponent_x:null,
        opponent_y:null,
        can_next:false,
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
        },
        updateColor(state, color){
            state.own_color = color.own_color;
            state.opponent_color = color.opponent_color;
        },
        updateOwnStep(state, step){
            state.own_x = step.own_x;
            state.own_y = step.own_y;
        },
        updateOpponentStep(state,step) {
            state.opponent_x = step.opponent_y;
            state.opponent_y = step.opponent_y;
        },
        updateCanStep(state,step){
            state.can_next = step;
        }

    },
    actions: {
    },
    modules: {
    }
}