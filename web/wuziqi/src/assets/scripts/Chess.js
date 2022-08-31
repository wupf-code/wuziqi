import {AcGameObject} from "@/assets/scripts/AcGameObject";

export class Chess extends AcGameObject{
    constructor(x,y,L,gamemap,color) {
        super();
        this.x = x;
        this.y = y;
        this.gamemap = gamemap;
        this.color = color;
        this.L=L;
    }
    update() {
        this.render();
    }

    render() {
        const L =this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.beginPath();
        ctx.arc(this.y*L, this.x*L, L / 2, 0, 2 * Math.PI, false);
        ctx.fillStyle = this.color;
        ctx.fill();
    }
}