import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-contact-list',
  templateUrl: './contact-list.component.html',
  styleUrls: ['./contact-list.component.css']
})
export class ContactListComponent implements OnInit {
  contacts: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchContacts();
  }

  fetchContacts() {
    this.http.get<any[]>('http://localhost:8080/api/contacts')
      .subscribe(data => this.contacts = data);
  }

  editContact(id: number) {
    // Navigate to edit page
  }

  deleteContact(id: number) {
    this.http.delete(`http://localhost:8080/api/contacts/${id}`)
      .subscribe(() => this.fetchContacts());
  }
}
