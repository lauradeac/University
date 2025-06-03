export class SongDTO {
  songId?: string | undefined;
  title!: string;
  artist!: string;
  duration!: number;
  genre!: string;
  playlistId!: string;
  isEditable?: boolean;

  constructor(
    title: string,
    artist: string,
    duration: number,
    genre: string,
    playlistId: string,
    songId?: string,
    isEditable?: boolean
  ) {
    this.title = title;
    this.artist = artist;
    this.duration = duration;
    this.genre = genre;
    this.playlistId = playlistId;
    this.songId = songId;
    this.isEditable = false;
  }
}
