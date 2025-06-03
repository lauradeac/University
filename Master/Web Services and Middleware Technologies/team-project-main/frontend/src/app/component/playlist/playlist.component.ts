import {ChangeDetectorRef, Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {PlaylistService} from "../../service/playlist.service";
import {PlaylistDTO} from "../../model/playlistDTO";
import {HttpErrorResponse} from "@angular/common/http";
import {SongDTO} from "../../model/songDTO";
import {MatTableDataSource} from "@angular/material/table";
import {NgForm} from "@angular/forms";
import {take} from "rxjs";

@Component({
  selector: 'app-playlist',
  templateUrl: './playlist.component.html',
  styleUrls: ['./playlist.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PlaylistComponent implements OnInit {

  playlistSongs: any[] = [];
  currentPlaylist: any;
  columnsToDisplay = ['title', 'artist', 'genre', 'duration', 'actions'];
  dataLoaded: boolean = false;
  genres: string[] = [];

  showAddRow: boolean = false;
  newSong!: SongDTO;

  newArtist: any;
  newGenre: any;
  newDuration: any;
  newTitle: any;

  @ViewChild('addSongForm') addSongForm!: NgForm;

  constructor(private playlistService: PlaylistService) {
  }

  ngOnInit(): void {
    this.getPlaylist();
    this.getGenres();
  }

  getPlaylist() {
    this.playlistService.getPlaylist(1)
      .subscribe((data: any) => {
          this.currentPlaylist = new PlaylistDTO(data.id, data.name, data.description, data.songList)
          this.getSongsForPlaylist(this.currentPlaylist.id);
        },
        (error: HttpErrorResponse) => {
          console.error('Error fetching playlist:', error.error.message);
        });
  }

  getSongsForPlaylist(playListId: number): void {
    this.playlistService.getAllSongsByPlaylistId(playListId).subscribe({
      next: (data) => {
        this.playlistSongs = [];
        if (data != null) {
          for (let song of data) {
            const songDto = new SongDTO(
              song.title,
              song.artist,
              song.duration,
              song.genre,
              song.playlistId,
              song.songId,
            );
            songDto.isEditable = false;
            this.playlistSongs.push(songDto);
            this.dataLoaded = true;
          }
        }
      }
    });
  }

  toggleAddRow(): void {
    this.showAddRow = !this.showAddRow;
  }

  addSong(): void {
    if (this.addSongForm.valid) {
      this.newSong = new SongDTO(this.newTitle, this.newArtist, this.newDuration, this.newGenre, this.currentPlaylist.id);

      this.playlistService.addSong(this.newSong).pipe(take(1))
        .subscribe({
          next: () => {
            this.addSongForm.resetForm();
            this.getSongsForPlaylist(this.currentPlaylist.id);
            this.showAddRow = false;
          },
          error: (err) => {
            console.error('Error adding song:', err.error);
          }
        });
    }
  }

  deleteSong(songId: number) {
    if (songId) {
      this.playlistService.deleteSong(songId).subscribe(() => {
        this.getSongsForPlaylist(this.currentPlaylist.id)
      });
    } else {
      console.log('Song ID is undefined');
    }
  }

  getGenres(): void {
    this.playlistService.getGenres().subscribe((data: any[]) => {
      this.genres = data;
    });
  }

  toggleEditRow(song: any) {
    song.isEditable = !song.isEditable;
  }

  editSong(song: SongDTO): void {
    console.log(song)
    this.playlistService.editSong(song).subscribe(() => {
        song.isEditable = false;
      },
      (error) => {
        console.error('Error updating song:', error);
      }
    );
  }

  saveChanges(song: SongDTO): void {
    this.editSong(song);
    song.isEditable = false;
  }

  cancelEdit(song: SongDTO): void {
    song.isEditable = false;
  }

}
