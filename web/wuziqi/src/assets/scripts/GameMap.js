import {AcGameObject} from "@/assets/scripts/AcGameObject";
import {Chess} from "@/assets/scripts/Chess";
// import {cell} from "@/assets/scripts/cell";
import {Tip} from "@/assets/scripts/tip";

// import {Chess} from "@/assets/scripts/Chess";
export class GameMap extends AcGameObject {
    constructor(ctx, parent, store) {
        super();
        this.ctx = ctx;
        this.parent = parent;
        this.L = 0;
        this.cols = 17;
        this.rows = 17;
        this.chessesown = [];
        this.chessesoppoent = [];
        this.g=Array(17).fill().map(() => Array(17).fill(0));
        this.store = store;
        this.x = 0;
        this.y = 0;
        this.tip = null;
    }



    setX(x) {
        this.x = x;
    }

    setY(y) {
        this.y = y;
    }


    start() {
        this.canNext=this.store.state.pk.can_next;
        this.add_listening_events();
        this.drawChessOpponent(this.x, this.y);
        this.drawChessOwn(this.x, this.y);
        // new cell(0,0,this);
    }

    update_size() {
        this.L = (Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        this.update_size();
        this.render();
        // this.drawChess();
        // this.add_listening_events();
    }

    drawChessOwn(x, y) {
            if(this.tip!=null){
                this.tip.destroy();
            }
            if(x!=0 && y!=0)
            this.chessesown.push(new Chess(x, y,this, this.store.state.pk.own_color));
            this.tip =  new Tip(x,y,this,'green');
            this.g[x][y]=1;
            this.canNext=false;

    }

    drawChessOpponent(x, y) {
            if(this.tip!=null){
                this.tip.destroy();
            }
            if(x!=0 && y!=0)
            this.chessesoppoent.push(new Chess(x, y, this, this.store.state.pk.opponent_color));
            this.tip =  new Tip(x,y,this,'green');
            this.g[x][y]=2;
            this.canNext=true;


    }

    drawChessBoard() {
        let size = this.cols;
        //画棋盘
        for (let i = 1; i < this.cols; i++) {
            this.ctx.beginPath();
            this.ctx.moveTo(this.L, i * this.L);
            this.ctx.lineTo((size - 1) * this.L, i * this.L);
            this.ctx.stroke();

            this.ctx.beginPath();
            this.ctx.moveTo(this.L * i, this.L);
            this.ctx.lineTo(i * this.L, this.L * (size - 1));
            this.ctx.stroke();
        }
    }

    add_listening_events() {
        this.ctx.canvas.focus();

        this.ctx.canvas.addEventListener('click', e => {

            this.L = (Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));

            let x = e.offsetX, y = e.offsetY;
            let r = Math.round(x / this.L);
            let c = Math.round(y / this.L);
            if (c > 0 && c < 17 && r > 0 && r < 17)
                // this.chessesBlack.push(new Chess(c,r,this,'black'));
                if(this.store.state.pk.can_next===true&&this.g[c][r]===0 )
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    x: c,
                    y: r,
                }))
        });


        // this.ctx.canvas.addEventListener('contextmenu',e =>{
        //     this.L = (Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        //
        //     let x =e.offsetX,y = e.offsetY;
        //     let r =Math.round(x/this.L);
        //     let c =Math.round(y/this.L);
        //     if(c>0&&c<17 && r >0 &&r <17)
        //         this.chessesWhite.push(new Chess(c,r,this,'white'));
        // })
    }


    render() {
        // this.ctx.fillStyle = "yellow";
        // this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
        this.drawChessBoard();
    }
}