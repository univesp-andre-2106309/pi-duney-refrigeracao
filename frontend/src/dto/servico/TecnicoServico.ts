import Tecnico from "../tecnico/Tecnico"

type TecnicoServico = {
    id: number,
    tecnico: Tecnico,
    dtCriacao?: string,
    descricao: string
}

export default TecnicoServico;