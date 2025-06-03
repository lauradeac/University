import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, Subject} from "rxjs";
import {EventDTO} from "../model/eventDTO.model";
const GRAPH_ENDPOINT = 'https://graph.microsoft.com/v1.0/me';
const GRAPH_ENDPOINT_PIC = 'https://graph.microsoft.com/v1.0/me/photo/$value';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  isUserLoggedIn: Subject<boolean> = new Subject<boolean>()

  constructor(private httpClient: HttpClient) { }

  addEvent(event: EventDTO): Observable<any> {
    console.log("Evenimentul de adăugat: service ", event);

    let formData: FormData = new FormData();
    formData.append('name', event.name);
    formData.append('location', event.location);
    formData.append('dateTime', event.dateTime);
    formData.append('category', event.category);
    formData.append('description', event.description);
    formData.append('imageUrl', event.imageUrl);

    if (event.file !== null && event.file !== undefined) {
      formData.append('file', event.file, event.file.name);
    }

    return this.httpClient.post<any>('http://localhost:8080/api/events/add', formData);
  }

  editEvent(event: EventDTO): Observable<any> {
    let formData: FormData = new FormData();
    if(event.eventId != null) {
      formData.append('eventId', event.eventId);
    }
    formData.append('name', event.name);
    formData.append('location', event.location);
    formData.append('dateTime', event.dateTime);
    formData.append('category', event.category);
    formData.append('description', event.description);
    formData.append('imageUrl', event.imageUrl);

    if (event.file !== null && event.file !== undefined) {
      formData.append('file', event.file, event.file.name);
    }

    return this.httpClient.put<any>('http://localhost:8080/api/events/edit', formData);
  }

  getEventById(id: number): Observable<EventDTO> {
    let url = "http://localhost:8080/api/events/getById/" + id;
    return this.httpClient.get<EventDTO>(url, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }

  getAllEvents(): Observable<EventDTO[]> {
    let url = "http://localhost:8080/api/events/all";
    return this.httpClient.get<EventDTO[]>(url, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }

  deleteEvent(eventId: number) {
    return this.httpClient.delete(`http://localhost:8080/api/events/delete/${eventId}`);
  }

  getCategories() {
    let url = "http://localhost:8080/api/events/categories";
    return this.httpClient.get<any[]>(url, {
      headers: {
        'Content-Type': 'application/json',
      }
    });
  }
}
