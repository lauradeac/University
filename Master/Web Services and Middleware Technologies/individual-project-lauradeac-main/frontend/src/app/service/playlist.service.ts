import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SongDTO} from "../model/songDTO";
import {PlaylistDTO} from "../model/playlistDTO";

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {

  constructor(private httpClient: HttpClient) { }

  addSong(song: SongDTO): Observable<any> {
    let url = "http://localhost:8080/api/playlist/add-song";
    return this.httpClient.post(url, song, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }

  editSong(song: SongDTO): Observable<any> {
    let url = "http://localhost:8080/api/playlist/edit-song";
    return this.httpClient.put(url, song, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }

  getPlaylist(id: number): Observable<PlaylistDTO> {
    let url = "http://localhost:8080/api/playlist/getById/" + id;
    return this.httpClient.get<PlaylistDTO>(url, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }

  getAllSongsByPlaylistId(playlistId: number): Observable<SongDTO[]> {
    let url = "http://localhost:8080/api/playlist/songs/" + playlistId;
    return this.httpClient.get<SongDTO[]>(url, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }

  deleteSong(songId: number) {
    let url = "http://localhost:8080/api/playlist/delete-song/" + songId;
    return this.httpClient.delete(url, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }

  getGenres() {
    let url = "http://localhost:8080/api/playlist/genres";
    return this.httpClient.get<any[]>(url, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }
}
