import {SongDTO} from "./songDTO";

export class PlaylistDTO {
  id!: string;
  name!: string;
  description!: string;
  songList!: SongDTO[];

  constructor(
    id: string,
    name: string,
    description: string,
    songList: SongDTO[]
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.songList = songList;
  }
}
