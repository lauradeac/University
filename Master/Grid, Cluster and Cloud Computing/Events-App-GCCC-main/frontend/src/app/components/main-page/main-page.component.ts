import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {EventService} from "../../service/event.service";
import {EventDTO} from "../../model/eventDTO.model";
import {format} from 'date-fns';
import {Router} from "@angular/router";
import {OAuthService} from "angular-oauth2-oidc";

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css'],
  encapsulation: ViewEncapsulation.None
})

export class MainPageComponent implements OnInit {

  events: any[] = [];
  dataLoaded: boolean = false;
  categories: string[] = [];

  constructor(private eventService: EventService,
              private router: Router,
              private oauthService: OAuthService) {
  }

  ngOnInit(): void {
    this.getAllEvents();
  }

  formatDate(dateTime: string): string {
    return format(new Date(dateTime), 'dd MMMM yyyy, HH:mm');
  }

  getAllEvents(): void {
    this.eventService.getAllEvents().subscribe({
      next: (data) => {
        this.events = [];
        if (data != null) {
          for (let event of data) {
            const eventDto = new EventDTO(
              event.name,
              event.location,
              event.dateTime,
              event.category,
              event.description,
              event.imageUrl,
            );
            eventDto.isEditable = false;
            this.events.push(event);
            this.dataLoaded = true;
          }
        }
      }
    });
  }

  getCategories(): void {
    this.eventService.getCategories().subscribe((data: any[]) => {
      this.categories = data;
    });
  }

  addEvent(): void {
    this.router.navigateByUrl('/add-event');
  }

  deleteEvent(eventId: string, index: number): void {
    console.log('Eveniment de sters ', eventId);
    if (confirm('Ești sigur că vrei să ștergi acest eveniment?')) {
      this.eventService.deleteEvent(Number(eventId)).subscribe(
        response => {
          console.log('Eveniment șters cu succes!');
          this.events.splice(index, 1); // Elimină evenimentul din listă
        },
        error => {
          console.error('Eroare la ștergerea evenimentului:', error);
        }
      );
    }
  }

  editEvent(eventId: string): void {
    this.router.navigate(['/edit-event', eventId]);
  }

  logout() {
    this.oauthService.logOut();
    this.router.navigate(['/']);
  }
}
