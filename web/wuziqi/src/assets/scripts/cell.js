import { AcGameObject} from "@/assets/scripts/AcGameObject";
export class cell extends  AcGameObject {
    constructor(x,y,gamemap) {
        super();
        this.x = x;
        this.y = y;
        this.gamemap=gamemap;
    }
    update() {
        this.render();
    }

    render() {
        const L =this.gamemap.L;
        const ctx = this.gamemap.ctx;
        ctx.fillStyle="#D38F3FFF"

        ctx.fillRect(this.x, this.y, L, L)
        // ctx.fill();
    }
}