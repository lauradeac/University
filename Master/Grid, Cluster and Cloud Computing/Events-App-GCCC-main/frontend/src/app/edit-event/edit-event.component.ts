import {Component, OnInit} from '@angular/core';
import {EventDTO} from "../model/eventDTO.model";
import {EventService} from "../service/event.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-edit-event',
  templateUrl: './edit-event.component.html',
  styleUrls: ['./edit-event.component.css']
})
export class EditEventComponent implements OnInit {

  event: EventDTO = new EventDTO('', '', '', '', '', '');
  imageFile: File | null = null;
  categories: string[] = [];
  notificationMessage: string | null = null;

  constructor(private eventService: EventService,
              private router: Router,
              private route: ActivatedRoute) {

  }

  ngOnInit(): void {
    const eventId = this.route.snapshot.params['id'];
    this.eventService.getEventById(eventId).subscribe(
      (data: EventDTO) => {
        this.event = data;
      },
      (error) => {
        console.error("Eroare la obținerea evenimentului:", error);
      }
    );
    this.getCategories();
  }


  submitForm(eventForm: any): void {
    if (eventForm.invalid) {
      return;
    }
    console.log("Eveniment de editat", this.event);
    this.eventService.editEvent(this.event).subscribe(
      () => {
        this.showNotification("Evenimentul a fost actualizat cu succes!");
        setTimeout(() => {
          this.router.navigate(['/main']);
        }, 3000);
      },
      error => {
        console.error("Eroare la actualizarea evenimentului:", error);
        this.showNotification("Eroare la actualizarea evenimentului.");
      }
    );
  }

  showNotification(message: string): void {
    this.notificationMessage = message;
    setTimeout(() => {
      this.notificationMessage = null;
    }, 2000);
  }

  handleFileInput(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.imageFile = file;
      this.event.file = file;
    }
  }

  getCategories(): void {
    this.eventService.getCategories().subscribe((data: string[]) => {
      this.categories = data;
    });
  }
}
